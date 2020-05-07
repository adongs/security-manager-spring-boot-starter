package com.adongs.implement.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.adongs.utils.el.ElAnalysis;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author adong
 * @version 1.0
 */
public class DefaultLogOutFormat implements LogFormat {

    private final  ObjectMapper OBJECT_MAPPER;
    private final static String INTERVAL = "    ";
    private final boolean lineFeed ;

    public DefaultLogOutFormat() {
        this(new ObjectMapper(),true);
    }

    public DefaultLogOutFormat(ObjectMapper objectMapper, boolean lineFeed) {
        OBJECT_MAPPER = objectMapper.copy();
        this.lineFeed = lineFeed;
    }

    public DefaultLogOutFormat(ObjectMapper OBJECT_MAPPER) {
        this(OBJECT_MAPPER,true);
    }

    public DefaultLogOutFormat(boolean lineFeed) {
        this(new ObjectMapper(),lineFeed);
    }

    @Override
    public StringBuffer format(LogOutInfo logOutInfo) {
        if(lineFeed){
            return lineFeed(logOutInfo);
        }else{
            return line(logOutInfo);
        }
    }


    @Override
    public StringBuffer customize(LogOutInfo logOutInfo, String content) {
        Map<String, Object> params = logOutInfo.getParamAndValue();
        if (!params.containsKey("return")){
            params.put("return",logOutInfo.getReturnInfo());
        }
        if (!params.containsKey("throw")){
            params.put("throw",logOutInfo.getThrowInfo());
        }
        ElAnalysis elAnalysis = new ElAnalysis(params);
        String analysis = elAnalysis.analysis(content);
        StringBuffer stringBuffer = new StringBuffer();
        if(!StringUtils.isEmpty(logOutInfo.getLabel())){
            stringBuffer.append(logOutInfo.getLabel()).append("  ");
        }
      return stringBuffer.append(analysis);
    }

    /**
     * 单行
     * @param logOutInfo LogOutInfo
     * @return 输出内容
     */
    private StringBuffer lineFeed(LogOutInfo logOutInfo){
        StringBuffer stringBuffer = new StringBuffer();
        if (!StringUtils.isEmpty(logOutInfo.getLabel())){
            stringBuffer.append(logOutInfo.getLabel()).append(INTERVAL);
        }else{
            stringBuffer.append(logOutInfo.getMethod().getName()).append(INTERVAL);
        }
        logOutInfo.getParamAndValue().forEach((k,v)->{
            stringBuffer.append(k).append("=").append(objectToJSON(v)).append(INTERVAL);
        });
        return stringBuffer;
    }

    /**
     * 多行
     * @param logOutInfo LogOutInfo
     * @return 输出内容
     */
    private StringBuffer line(LogOutInfo logOutInfo){
        StringBuffer stringBuffer = new StringBuffer();
        if (!StringUtils.isEmpty(logOutInfo.getLabel())){
            stringBuffer.append(logOutInfo.getLabel()).append("{").append("\n");
        }
        logOutInfo.getParamAndValue().forEach((k,v)->{
            stringBuffer.append(k).append("=").append(objectToJSON(v)).append("\n");
        });
        if(logOutInfo.getReturnInfo()!=null) {
            stringBuffer.append("return=").append(objectToJSON(logOutInfo.getReturnInfo())).append("\n");
        }else{
            stringBuffer.append("return=void").append("\n");
        }
        if (logOutInfo.getThrowInfo()!=null){
            stringBuffer.append("throw=").append(logOutInfo.getThrowInfo().getMessage()).append("\n");
        }
        stringBuffer.append("}");
        return stringBuffer;
    }


    /**
     * 转换
     * @param object 对象
     * @return 对象转json后的字符串
     */
    private String objectToJSON(Object object){
        String value = null;
        try {
            value = OBJECT_MAPPER.writeValueAsString(object);
        }catch (Exception e){
            value = object.toString();
        }
        return value;
    }
}
