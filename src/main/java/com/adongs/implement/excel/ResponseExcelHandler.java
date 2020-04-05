package com.adongs.implement.excel;

import com.adongs.annotation.extend.ResponseExcel;
import com.adongs.utils.el.ElAnalysis;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

/**
 * 数据导出处理器
 * @author yudong
 * @version 1.0
 */
public class ResponseExcelHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return methodParameter.getMethodAnnotation(ResponseExcel.class)!=null;
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
                  if (o==null){return;}
        ResponseExcel responseExcel = methodParameter.getMethodAnnotation(ResponseExcel.class);
        Collection<?> collection = getCollection(responseExcel, o);
        HttpServletResponse nativeResponse = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        HttpServletRequest nativeRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String fileName = getFileName(responseExcel, o);
        nativeResponse.setContentType("application/octet-stream");
        export(responseExcel,fileName,collection,nativeRequest,nativeResponse);
    }

    /**
     * 判断是否为浏览器
     * @param request 请求体
     * @return ture浏览器  false非浏览器
     */
    private boolean isBrowser(HttpServletRequest request){
        String header = request.getHeader("User-Agent");
        if (StringUtils.isEmpty(header)){
            return false;
        }
        return true;
    }


    /**
     * 导出数据
     * @param responseExcel
     * @param collection
     * @param response
     */
    private void export(ResponseExcel responseExcel, String fileName, Collection<?> collection, HttpServletRequest nativeRequest,HttpServletResponse response) throws IOException {
        boolean compression = responseExcel.compression();
        String sheelName = getSheelName(responseExcel, responseExcel.sheetName());
        String excelFileSuffix = getExcelFileSuffix(collection);
        if (compression){
            String name = isBrowser(nativeRequest)?URLEncoder.encode(fileName+".zip","UTF-8"):fileName+".zip";
            response.setHeader("Content-Disposition", "attachment;filename="+name);
            ByteArrayOutputStream export = ExcelFileProcessor.exportMemory(collection, fileName, sheelName);
            CompressionProcessor.fileCompress(export,fileName+excelFileSuffix,response);
        }else{
            String name = isBrowser(nativeRequest)?URLEncoder.encode(fileName+excelFileSuffix,"UTF-8"):fileName+excelFileSuffix;
            response.setHeader("Content-Disposition", "attachment;filename="+name);
            ExcelFileProcessor.export(collection,sheelName,response);
        }
    }


    /**
     * 获取excel文件后缀
     * @param collection
     * @return
     */
    private String getExcelFileSuffix(Collection<?> collection){
        if (collection.size()>10000){
            return ".xls";
        }
        return ".xlsx";
    }

    /**
     * 获取文件名称
     * @param responseExcel excel注解
     * @param o 返回值
     * @return 文件名称
     */
    private String getFileName(ResponseExcel responseExcel, Object o){
        String name = responseExcel.name();
        int datetimePosition = responseExcel.nameDatetimePosition();
        int isel = name.indexOf("#");
        if (isel!=-1){
            Map<String,Object> param = Maps.newHashMap();
            param.put("return",o);
            ElAnalysis elAnalysis = new ElAnalysis(param);
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
     * @param o  返回值
     * @return shell名称
     */
    private String getSheelName(ResponseExcel responseExcel, Object o){
        String sheelName= responseExcel.sheetName();
        int isel = sheelName.indexOf("#");
        if (isel!=-1){
            Map<String,Object> param = Maps.newHashMap();
            param.put("return",o);
            ElAnalysis elAnalysis = new ElAnalysis(param);
            sheelName = elAnalysis.analysis(sheelName);
        }
        return sheelName;
    }


    /**
     * 从object对象中提取集合
     * @param responseExcel
     * @param data
     * @return
     */
    private Collection<?> getCollection(ResponseExcel responseExcel, Object data){
        String datael = responseExcel.data();
        if (StringUtils.isEmpty(datael)){
            if (data instanceof  Collection<?>){
                return (Collection<?>)data;
            }
            return Lists.newArrayList();
        }else{
            Map<String,Object> param = Maps.newHashMap();
            param.put("return",data);
            ElAnalysis elAnalysis = new ElAnalysis(param);
            Collection analysis = elAnalysis.analysis(datael, Collection.class);
            return analysis;
        }
    }



}
