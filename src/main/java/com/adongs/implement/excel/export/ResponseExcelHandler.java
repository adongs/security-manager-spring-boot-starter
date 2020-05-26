package com.adongs.implement.excel.export;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adongs.annotation.extend.excel.ResponseExcel;
import com.adongs.annotation.extend.excel.Sheet;
import com.adongs.config.ExcelConfig;
import com.adongs.implement.excel.export.compression.Compression;
import com.adongs.implement.excel.export.compression.CompressionManager;
import com.adongs.session.terminal.Terminal;
import com.adongs.utils.el.ElAnalysis;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 数据导出处理器
 * @author yudong
 * @version 1.0
 */
public class ResponseExcelHandler implements HandlerMethodReturnValueHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseExcelHandler.class);

    /**
     * 文件扩展后缀
     */
   private final static ImmutableMap<String,String> CONTENT_TYPE_FILE_SUFFIX = ImmutableMap.<String,String>builder()
            .put("zip","application/zip")
            .put("tar","application/x-tar")
            .put("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .put("xls","application/vnd.ms-excel")
            .put("doc","application/msword")
            .put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            .build();
    /**
     * 默认响应方式
     */
   private final static String DEFAULT_CONTENT_TYPE_FILE_SUFFIX = "application/octet-stream";
    /**
     * 默认编码格式
     */
   private final static String CHARACTER_ENCODING = "UTF-8";

   private final ExcelFactory excelFactory;

    private final ApplicationContext context;

    private final CompressionManager compressionManager;

    private final ExcelConfig config;

    public ResponseExcelHandler(ApplicationContext applicationContext,ExcelConfig config,ExcelFactory excelFactory,CompressionManager compressionManager) {
        this.context = applicationContext;
        this.excelFactory = excelFactory;
        this.compressionManager = compressionManager;
        this.config = config;
    }

    /**
     * ResponseExcel 注解支持返回值为Collection 或者 ResponseEntity类型的controller
     * @param methodParameter 方法参数
     * @return ture支持处理 false不支持处理
     */
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        ResponseExcel responseExcel = methodParameter.getMethodAnnotation(ResponseExcel.class);
        if (responseExcel==null){
            return false;
        }
        final String path = responseExcel.path();
        if (!StringUtils.isEmpty(path)){
             String requestURI = Terminal.getRequest().getRequestURI();
            requestURI = requestURI.substring(requestURI.lastIndexOf("/")+1);
            if (!path.equals(requestURI)){
               return false;
            }
        }
        boolean isVoid = methodParameter.getParameterType().getName().equals(Void.TYPE.getName());
        if (isVoid){
            return false;
        }
        return true;
    }


    @Override
    public void handleReturnValue(@NonNull Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        ResponseExcel responseExcel = methodParameter.getMethodAnnotation(ResponseExcel.class);
        ExcelType excelType = StringUtils.isEmpty(responseExcel.excelType())?config.getExcelType():responseExcel.excelType().equals("HSSF")?ExcelType.HSSF:ExcelType.XSSF;
        Object data = o instanceof ResponseEntity?((ResponseEntity) o).getBody():o;
        Map<String,Object> elParam = new HashMap<String,Object>(1){{
            put("return",data);
        }};
        ElAnalysis elAnalysis = new ElAnalysis(elParam);
        autoAssembly(o,responseExcel,response);
        final String[] fileName = fileName(responseExcel,excelType, elAnalysis);
        final List<ExportView> exportViews = getData(responseExcel, excelType,data, elAnalysis);
        final Compression compression =StringUtils.isEmpty(responseExcel.excelType())?null:compressionManager.getType(responseExcel.compression());
        fileHeaderAuto(fileName[0],compression==null?fileName[1]:compression.compressType(),response);
        final Workbook export = excelFactory.export(exportViews, excelType, config.getRelease());
        ServletOutputStream outputStream = response.getOutputStream();
        if (compression!=null){
            compression.compress(export,String.join(".",fileName),outputStream);
        }else{
            export.write(outputStream);
            export.close();
        }
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 获取文件名称
     * @param excel 注解
     * @param el el解析器
     * @return 数组  [0]文件名称   [1]文件后缀名
     */
    public String [] fileName(ResponseExcel excel,ExcelType excelType,ElAnalysis el){
        String name = excel.name().indexOf("#")==-1?excel.name():el.analysis(excel.name());
        int datetimePosition = excel.position()==-2?config.getPosition():excel.position();
        String format = StringUtils.isEmpty(excel.format())?config.getFormat():excel.format();
        String suffix = excelType == ExcelType.XSSF?"xlsx":"xls";
        if (datetimePosition==0){
            return new String []{name,suffix};
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        String localTime = df.format(LocalDateTime.now());
        if (datetimePosition==-1){
            name =  localTime+name;
        }
        if (datetimePosition==1){
            name =  name+localTime;
        }
        return new String []{name,suffix};
    }

    /**
     * 获取数据
     * @param responseExcel excel注解
     * @param o 返回值
     * @param el el表达式解析
     * @return 数据
     */
    private List<ExportView> getData(ResponseExcel responseExcel,ExcelType excelType,Object o,ElAnalysis el){
        List<ExportView> exportViews = Lists.newArrayList();
        final Sheet[] sheets = responseExcel.sheet();
        for (Sheet sheet : sheets) {
            final Collection collection = StringUtils.isEmpty(sheet.data())?(Collection)o:el.analysis(sheet.data(),Collection.class);
            final ExportParams exportParams = excelFactory.exportParams(sheet,excelType);
            ExportView exportView = ExportView.build(exportParams,collection);
            exportViews.add(exportView);
        }
        return exportViews;
    }

    /**
     * 文件header自动填充
     * 编码格式,文件大小,返回格式,文件名称
     * @param fileFormat 文件格式
     * @param fileName 文件名称
     * @param nativeResponse 响应对象
     */
    private void fileHeaderAuto(String fileName,String fileFormat,HttpServletResponse nativeResponse)throws UnsupportedEncodingException{
        fileName = fileName+"."+fileFormat;
        HttpServletRequest request = Terminal.getRequest(HttpServletRequest.class);
        String header = request.getHeader("User-Agent");
        if (StringUtils.isEmpty(header)){
            fileName = URLEncoder.encode(fileName, CHARACTER_ENCODING);
        }
        if (nativeResponse.getCharacterEncoding().equals("ISO-8859-1")){
            nativeResponse.setCharacterEncoding(CHARACTER_ENCODING);
        }
        nativeResponse.setContentType(CONTENT_TYPE_FILE_SUFFIX.getOrDefault(fileFormat, DEFAULT_CONTENT_TYPE_FILE_SUFFIX));
        nativeResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+fileName);
    }

    /**
     * 获取返回值对象
     * @param methodParameter 方法
     * @return 返回值
     */
    private Object getReturnValue(MethodParameter methodParameter){
        try {
            Field returnValue = methodParameter.getClass().getDeclaredField("returnValue");
            if (returnValue!=null){
                returnValue.setAccessible(true);
                return returnValue.get(methodParameter);
            }
        } catch (NoSuchFieldException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 自动装配header
     * @param responseExcel excel注解
     * @param entity 响应对象
     * @param responseExcel 导出注解
     * @param response 响应体
     */
    private void autoAssembly(Object entity,ResponseExcel responseExcel,HttpServletResponse response){
        Set<String> headersStr = Sets.newHashSet(responseExcel.headers());
        ResponseEntity responseEntity =null;
        if (entity instanceof ResponseEntity){
            responseEntity = (ResponseEntity) entity;
        }
        headersStr.addAll(Sets.newHashSet(config.getHeaders()));
        final HttpHeaders headers =responseEntity==null?new HttpHeaders():responseEntity.getHeaders();
        if (!headersStr.isEmpty()){
            for (String s : headersStr) {
                String[] split = s.split(":");
                if (split.length==2) {
                    headers.add(split[0],split[1]);
                }
            }
        }
        headers.forEach((k,v)->response.setHeader(k,String.join(",",v)));
        if (responseEntity!=null){
            response.setStatus(responseEntity.getStatusCodeValue());
        }
    }
}
