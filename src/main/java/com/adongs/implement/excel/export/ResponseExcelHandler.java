package com.adongs.implement.excel.export;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adongs.annotation.extend.excel.ResponseExcel;
import com.adongs.implement.excel.export.compression.Compression;
import com.adongs.implement.excel.export.compression.CompressionManager;
import com.adongs.session.user.Terminal;
import com.adongs.utils.el.ElAnalysis;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 数据导出处理器
 * @author yudong
 * @version 1.0
 */
public class ResponseExcelHandler implements HandlerMethodReturnValueHandler {

    /**
     * 文件扩展后缀
     */
   private final static ImmutableMap<String,String> CONTENT_TYPE_FILE_SUFFIX = ImmutableMap.<String,String>builder()
            .put("zip","application/zip")
            .put("tar","application/x-tar")
            .put("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .put("xls","application/vnd.ms-excel")
            .put("doc","application/msword")
            .put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document").build();
    /**
     * 默认响应方式
     */
   private final static String DEFAULT_CONTENT_TYPE_FILE_SUFFIX = "application/octet-stream";
    /**
     * 默认编码格式
     */
   private final static String CHARACTER_ENCODING = "UTF-8";

   private final ExcelProcessor EXCEL_PROCESSOR;

    private final ApplicationContext APPLICATION_CONTEXT;

    private final CompressionManager COMPRESSION_MANAGER;

    public ResponseExcelHandler(ApplicationContext applicationContext,CompressionManager compressionManager) {
        this.APPLICATION_CONTEXT = applicationContext;
        this.EXCEL_PROCESSOR = new ExcelProcessor(applicationContext);
        this.COMPRESSION_MANAGER = compressionManager;
    }

    /**
     * ResponseExcel 注解支持返回值为Collection 或者 ResponseEntity类型的controller
     * @param methodParameter 方法参数
     * @return ture支持处理 false不支持处理
     */
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        boolean hasMethodAnnotation = methodParameter.hasMethodAnnotation(ResponseExcel.class);
        if (!hasMethodAnnotation){
            return false;
        }
        Class<?> returnType = methodParameter.getMethod().getReturnType();
        boolean expectationType =Collection.class.isAssignableFrom(returnType);
        if (!expectationType){
            expectationType = ResponseEntity.class.isAssignableFrom(returnType);
        }
        return expectationType;
    }


    @Override
    public void handleReturnValue(@NonNull Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        ResponseExcel responseExcel = methodParameter.getMethodAnnotation(ResponseExcel.class);
        Map<String,Object> elParam = new HashMap<String,Object>(1){{
            put("return",o instanceof ResponseEntity?((ResponseEntity) o).getBody():o);
        }};
        ElAnalysis elAnalysis = new ElAnalysis(elParam);
        String fileName = getFileName(responseExcel, elAnalysis);
        String fileSuffix = getFileSuffix(responseExcel);
        Collection data = getData(responseExcel,o,elAnalysis);
        String sheelName = getSheelName(responseExcel, elAnalysis);
        String title = getTitle(responseExcel, elAnalysis);
        ExportParams exportParams = EXCEL_PROCESSOR.exportParams(title, sheelName, responseExcel);
        if (o instanceof ResponseEntity){
            autoAssembly((ResponseEntity)o,fileSuffix,fileName,response);
        }else{
            autoAssembly(responseExcel,fileSuffix,fileName,response);
        }
        excel(responseExcel, fileName, exportParams, data, response);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 写入数据
     * @param responseExcel excel注解
     * @param collection 数据
     * @param response 响应体
     * @return 文件大小
     */
    private void excel(ResponseExcel responseExcel,String fileName,ExportParams exportParams, Collection<?> collection, HttpServletResponse response) throws IOException {
        try(Workbook export = EXCEL_PROCESSOR.export(exportParams, collection);
            ServletOutputStream outputStream = response.getOutputStream()) {
            if (responseExcel.compression()) {
                Compression compression = COMPRESSION_MANAGER.getType(responseExcel.compressionFormat());
                String suffix = responseExcel.excelType() == ExcelType.XSSF ? ".xlsx" : ".xls";
                compression.compress(export,fileName+suffix,outputStream);
            } else {
                export.write(outputStream);
                outputStream.flush();
            }
        }
    }

    /**
     * 自动装配header
     * @param responseEntity 返回对象
     * @param fileFormat 文件格式
     * @param fileName 文件名称
     * @param nativeResponse 响应体
     * @throws UnsupportedEncodingException 编码异常
     */
    private void autoAssembly(ResponseEntity responseEntity,String fileFormat,String fileName,HttpServletResponse nativeResponse)throws UnsupportedEncodingException{
        HttpHeaders headers = responseEntity.getHeaders();
        headers.forEach((k,v)->{
            nativeResponse.setHeader(k,String.join(",",v));
        });
        nativeResponse.setStatus(responseEntity.getStatusCodeValue());
        fileHeaderAuto(fileFormat,fileName,nativeResponse);
    }

    /**
     * 自动装配header
     * @param responseExcel excel注解
     * @param fileFormat 文件格式
     * @param fileName 文件名称
     * @param nativeResponse 响应体
     * @throws UnsupportedEncodingException 编码异常
     */
    private void autoAssembly(ResponseExcel responseExcel,String fileFormat,String fileName,HttpServletResponse nativeResponse) throws UnsupportedEncodingException {
        for (String header : responseExcel.headers()) {
            String[] split = header.split(":");
            if (split.length==2) {
                nativeResponse.setHeader(split[0],split[1]);
            }
        }
        fileHeaderAuto(fileFormat,fileName,nativeResponse);
    }

    /**
     * 获取表格标题
     * @param responseExcel excel注解
     * @param elAnalysis el解析器
     * @return 标题
     */
    private String getTitle(ResponseExcel responseExcel,ElAnalysis elAnalysis){
        String title = responseExcel.title();
        if (StringUtils.isEmpty(title)){
            return null;
        }
        if (title.indexOf("#")!=-1){
          return elAnalysis.analysis(title);
        }
        return title;
    }

    /**
     * 获取数据
     * @param responseExcel excel注解
     * @param o 返回值
     * @param elAnalysis el表达式解析
     * @return 数据
     */
    private Collection getData(ResponseExcel responseExcel,Object o,ElAnalysis elAnalysis){
        if (StringUtils.isEmpty(responseExcel.data())){
            if (o instanceof ResponseEntity){
                ResponseEntity responseEntity = (ResponseEntity)o;
                Object body = responseEntity.getBody();
                if (body instanceof Collection){
                    return (Collection)body;
                }else{
                    throw new ClassCastException("return data type "+o.getClass().getName()+" Expectation type Collection");
                }
            }else if(o instanceof Collection){
                return (Collection)o;
            }
        }else{
           return elAnalysis.analysis(responseExcel.name(), Collection.class);
        }
         throw new NullPointerException("Can't get data");
    }

    /**
     * 文件header自动填充
     * 编码格式,文件大小,返回格式,文件名称
     * @param fileFormat 文件格式
     * @param fileName 文件名称
     * @param nativeResponse 响应对象
     */
    private void fileHeaderAuto(String fileFormat,String fileName,HttpServletResponse nativeResponse)throws UnsupportedEncodingException{
          fileName = fileName+"."+fileFormat;
       if (isBrowser()){
           fileName = URLEncoder.encode(fileName, CHARACTER_ENCODING);
       }
       if (nativeResponse.getCharacterEncoding().equals("ISO-8859-1")){
           nativeResponse.setCharacterEncoding(CHARACTER_ENCODING);
       }
       nativeResponse.setContentType(CONTENT_TYPE_FILE_SUFFIX.getOrDefault(fileFormat, DEFAULT_CONTENT_TYPE_FILE_SUFFIX));
       nativeResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+fileName);
   }

    /**
     * 判断是否为浏览器
     * 主要用来区分postman和浏览器
     * @return ture浏览器  false非浏览器
     */
    private boolean isBrowser(){
        HttpServletRequest request = Terminal.getRequest(HttpServletRequest.class);
        String header = request.getHeader("User-Agent");
        if (StringUtils.isEmpty(header)){
            return false;
        }
        return true;
    }

    /**
     * 获取文件后缀
     * @param responseExcel 导出excel注解
     * @return 后缀类型
     */
    private String getFileSuffix(ResponseExcel responseExcel){
        if (responseExcel.compression()){
           return responseExcel.compressionFormat();
        }
        if(responseExcel.excelType()== ExcelType.XSSF){
            return "xlsx";
        }
       return "xls";
    }

    /**
     * 获取文件名称
     * @param responseExcel excel注解
     * @param elAnalysis el表达式解析器
     * @return 文件名称
     */
    private String getFileName(ResponseExcel responseExcel, ElAnalysis elAnalysis){
        String name = responseExcel.name();
        int datetimePosition = responseExcel.nameDatetimePosition();
        int isel = name.indexOf("#");
        if (isel!=-1){
            name = elAnalysis.analysis(name);
        }
        if (datetimePosition==0){
            return name;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(responseExcel.nameDatetime());
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        if (datetimePosition==-1){
             name =  localTime+name;
        }
        if (datetimePosition==1){
            name =  name+localTime;
        }
        return name;
    }

    /**
     * 获取sheel名称
     * @param responseExcel excel注解
     * @param elAnalysis  el表达式解析器
     * @return shell名称
     */
    private String getSheelName(ResponseExcel responseExcel,ElAnalysis elAnalysis){
        String sheelName= responseExcel.sheetName();
        int isel = sheelName.indexOf("#");
        if (isel!=-1){
            sheelName = elAnalysis.analysis(sheelName);
        }
        return sheelName;
    }



}
