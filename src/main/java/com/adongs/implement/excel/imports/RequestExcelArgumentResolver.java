package com.adongs.implement.excel.imports;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adongs.annotation.extend.RequestExcel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * 处理和保存excel文件
 * @author yudong
 * @version 1.0
 */
public class RequestExcelArgumentResolver implements HandlerMethodArgumentResolver {


    private ApplicationContext applicationContext;

    public RequestExcelArgumentResolver(ApplicationContext applicationContext) {
        this.applicationContext =applicationContext;
    }

    /**
     * 是否与给定方法的参数是由该解析器的支持
     * @param methodParameter 方法参数
     * @return true处理   false不处理
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> nestedParameterType = methodParameter.getNestedParameterType();
        boolean isList = List.class.isAssignableFrom(nestedParameterType);
        ParameterizedType parameterizedType = (ParameterizedType)methodParameter.getGenericParameterType();
        boolean isGeneric= parameterizedType.getActualTypeArguments().length == 1;
        return isList&&isGeneric&&methodParameter.hasParameterAnnotation(RequestExcel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        StandardMultipartHttpServletRequest nativeRequest = nativeWebRequest.getNativeRequest(StandardMultipartHttpServletRequest.class);
        List list = new ArrayList(512);
        ParameterizedType parameterizedType = (ParameterizedType)methodParameter.getGenericParameterType();
        Class actualTypeArgument = (Class)parameterizedType.getActualTypeArguments()[0];
        RequestExcel requestExcel = methodParameter.getParameterAnnotation(RequestExcel.class);
        ExcelProcessor bean = applicationContext.getBean(requestExcel.processor());
        Collection<MultipartFile> values = StringUtils.isEmpty(requestExcel.name())?nativeRequest.getFileMap().values():nativeRequest.getFiles(requestExcel.name());
        if (requestExcel.check()){
            listCheck(values,bean,list,actualTypeArgument,requestExcel);
        }else{
            list(values,list,actualTypeArgument);
        }
        bean.saveFile(requestExcel.savePath(),values);
        return list;
    }


    /**
     * 导入数据不进行校验
     */
    private void list(Collection<MultipartFile> collection,List list,Class<?> clazz) throws Exception {
        for (Iterator<MultipartFile> iterator = collection.iterator();iterator.hasNext();){
            List<Object> objects = ExcelImportUtil.importExcel(iterator.next().getInputStream(), clazz, new ImportParams());
            list.addAll(objects);
        }
    }

    /**
     * 导入数据进行校验
     * @param collection 文件集
     * @param bean 处理器
     * @param list 数据集
     * @param clazz 转换类型
     * @param requestExcel excel注解
     * @throws Exception 异常
     */
    private void listCheck(Collection<MultipartFile> collection,ExcelProcessor bean,List list,Class clazz,RequestExcel requestExcel)throws Exception{
        ImportParams params = new ImportParams();
        if (requestExcel.check()){
            params.setNeedVerify(true);
        }
        if (requestExcel.group().length>0){
            params.setVerifyGroup(requestExcel.group());
        }
        for (Iterator<MultipartFile> iterator = collection.iterator();iterator.hasNext();){
            ExcelImportResult<Object> excelImportResult = ExcelImportUtil.importExcelMore(iterator.next().getInputStream(), clazz, params);
            if(excelImportResult.isVerifyFail()){
                bean.check(excelImportResult);
            }
            list.addAll(excelImportResult.getList());
        }
    }

}
