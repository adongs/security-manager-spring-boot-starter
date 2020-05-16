package com.adongs.implement.sign;

import com.adongs.constant.ResolutionLocation;
import com.adongs.session.terminal.Terminal;
import com.adongs.utils.el.ElAnalysis;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adongs.implement.decrypt.coding.MD5;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 默认盐值校验
 */
public class DefaultSignProcessor implements SignProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSignProcessor.class);
    private static final MD5 md5 = MD5.singleBuild();
    private final Map<String, ResolutionLocation> locationMap = Maps.newHashMap();
    private final static String SIGN ="sign";
    @Autowired
    private ObjectMapper mapper;


    public DefaultSignProcessor() {
        locationMap.put("POST",ResolutionLocation.BODY);
        locationMap.put("GET",ResolutionLocation.PARAM);
        locationMap.put("PUT",ResolutionLocation.BODY);
        locationMap.put("DELETE",ResolutionLocation.BODY);
    }

    public DefaultSignProcessor(Map<String, ResolutionLocation> locationMap) {
        locationMap.putAll(locationMap);
    }

    @Override
    public String sign(String sign, Map<String, Object> param) {
        if (StringUtils.isEmpty(sign)){
            ElAnalysis elAnalysis = new ElAnalysis(param);
            return elAnalysis.analysis(sign, String.class);
        }
        final Terminal terminal = Terminal.get();
        final List<String> header= terminal.header("SIGN");
        return header==null?"":header.iterator().next();
    }

    @Override
    public String calculate(Map<String, Object> params) {
        final Terminal terminal = Terminal.get();
        final HttpServletRequest request = terminal.getRequest();
        ResolutionLocation location = locationMap.get(request.getMethod());
        if (location==null){
            throw new NullPointerException("没有找到解析位置");
        }
        if (location == ResolutionLocation.BODY){
            if (request instanceof ContentCachingRequestWrapper){
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper)request;
                try {
                    return sign(wrapper);
                }catch (IOException ioException){
                    return "";
                }
            }else{
                LOGGER.error("没有开启request body 重复读取");
                return "";
            }
        }
        if (location == ResolutionLocation.PARAM){
            return sign(request);
        }
        return "";
    }

    /**
     * 计算sign,获取body中的值 只支持 application/json
     * @param wrapper 重复读取body
     * @return sign
     * @throws IOException
     */
    private String sign(ContentCachingRequestWrapper wrapper) throws IOException {
        StringBuilder param = new StringBuilder();
        Map<String,String> map = Maps.newTreeMap();
        final byte[] contentAsByteArray = wrapper.getContentAsByteArray();
         JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(contentAsByteArray);
        } catch (IOException e) {
            LOGGER.error("解析数据异常 data={}",new String(contentAsByteArray),e);
            throw e;
        }
        final Iterator<String> iterator = jsonNode.fieldNames();
        while (iterator.hasNext()){
            final String next = iterator.next();
            final JsonNode parent = jsonNode.findParent(next);
            boolean isAllowed = parent.isBoolean() ||
                    parent.isBigDecimal() ||
                    parent.isBigInteger()||
                    parent.isDouble()||
                    parent.isFloat()||
                    parent.isInt()||
                    parent.isBigInteger()||
                    parent.isTextual()||
                    parent.isLong()||
                    parent.isShort();
            if (isAllowed) {
                map.put(next, parent.toString());
            }
        }
        map.forEach((k,v)->param.append(v));
        final Terminal terminal = Terminal.get();
       return md5.encode(param.append(terminal.token().getToken()).toString());
    }

    /**
     * 计算sign,获取param中的值
     * @param request HttpServletRequest
     * @return sign
     */
    private String sign(HttpServletRequest request){
        final Map<String, String[]> parameterMap = Maps.newTreeMap();
        parameterMap.putAll(request.getParameterMap());
        StringBuilder param = new StringBuilder();
        parameterMap.forEach((k,v)->param.append(StringUtils.join(v)));
        final Terminal terminal = Terminal.get();
        return   md5.encode(param.append(terminal.token().getToken()).toString());
    }


    @Override
    public String name() {
        return "default";
    }


}
