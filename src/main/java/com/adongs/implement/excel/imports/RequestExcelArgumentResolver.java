package com.adongs.implement.excel.imports;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.adongs.annotation.extend.excel.RequestExcel;
import com.adongs.config.ExcelConfig;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * 处理和保存excel文件
 * @author yudong
 * @version 1.0
 */
public class RequestExcelArgumentResolver implements HandlerMethodArgumentResolver {

    private static final ThreadLocal<Map<String,Set<String>>> threadLocal = new ThreadLocal();
    private static final Map<String,ExcelProcessor> PROCESSOR_MAP = Maps.newHashMap();
    private final ApplicationContext applicationContext;
    private final ExportReadFactory exportReadFactory;
    private final ExcelConfig config;

    public RequestExcelArgumentResolver(ApplicationContext applicationContext, ExportReadFactory exportReadFactory, ExcelConfig config) {
        this.applicationContext = applicationContext;
        this.exportReadFactory = exportReadFactory;
        this.config = config;
    }

    /**
     * 是否与给定方法的参数是由该解析器的支持
     * @param methodParameter 方法参数
     * @return true处理   false不处理
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        boolean hasParameterAnnotation = methodParameter.hasParameterAnnotation(RequestExcel.class);
        if (!hasParameterAnnotation){
            return false;
        }
        Class<?> nestedParameterType = methodParameter.getNestedParameterType();
        boolean isList = List.class.isAssignableFrom(nestedParameterType);
        ParameterizedType parameterizedType = (ParameterizedType)methodParameter.getGenericParameterType();
        boolean isGeneric= parameterizedType.getActualTypeArguments().length == 1;
        return isList&&isGeneric;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        RequestExcel requestExcel = methodParameter.getParameterAnnotation(RequestExcel.class);
        StandardMultipartHttpServletRequest nativeRequest = nativeWebRequest.getNativeRequest(StandardMultipartHttpServletRequest.class);
        ParameterizedType parameterizedType = (ParameterizedType)methodParameter.getGenericParameterType();
        Class actualTypeArgument = (Class)parameterizedType.getActualTypeArguments()[0];
        final Map<String, MultipartFile> fileMap = nativeRequest.getFileMap();
        final MultipartFile multipartFile = fileMap.get(requestExcel.name());
        if (multipartFile==null){
            return null;
        }
        final Map<String, Integer> sheets = exportReadFactory.readSheet(multipartFile);
        if (sheets==null){
            return null;
        }
        final ImportParams importParams = exportReadFactory.importParams(requestExcel, sheets);
        final List read = exportReadFactory.read(multipartFile, actualTypeArgument, importParams);
        final boolean save = isSave(nativeRequest, multipartFile);
        if (!save){
            String name = StringUtils.isEmpty(requestExcel.name())?config.getImportProcessor():requestExcel.processor();
            final ExcelProcessor processor = processor(name);
            processor.saveFile(requestExcel.savePath(),multipartFile);
        }
        return read;
    }


    private ExcelProcessor processor(String name){
        if (StringUtils.isEmpty(name)){
            name = config.getImportProcessor();
        }
        if (PROCESSOR_MAP.isEmpty()){
            final Map<String, ExcelProcessor> beansOfType = applicationContext.getBeansOfType(ExcelProcessor.class);
            if (beansOfType!=null && !beansOfType.isEmpty()){
                for (ExcelProcessor value : beansOfType.values()) {
                    PROCESSOR_MAP.put(value.name(),value);
                }
            }
        }
        return PROCESSOR_MAP.get(name);
    }

    private boolean isSave(HttpServletRequest request,MultipartFile file){
         Map<String, Set<String>> stringSetMap = threadLocal.get();
        if (stringSetMap==null){
            stringSetMap = Maps.newHashMap();
        }
        Set<String> strings = stringSetMap.get(request.toString());
        if (strings==null){
            strings = Sets.newHashSet();
            strings.add(file.getOriginalFilename());
            stringSetMap.put(request.toString(),strings);
            threadLocal.remove();
            threadLocal.set(stringSetMap);
            return false;
        }else{
            return strings.contains(file.getOriginalFilename());
        }
    }

}
