package com.security.manager.implement.sign;

import com.google.common.collect.Maps;
import com.security.manager.exception.SignException;
import com.security.manager.implement.decrypt.coding.MD5;
import com.security.manager.session.user.Terminal;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class DefaultSignProcessor implements SignProcessor {
    private static final MD5 md5 = MD5.build();


    @Override
    public void check(String sign, Object... args) {
        boolean emptySign = StringUtils.isEmpty(sign);
        if (emptySign){
            throw new SignException("sign mismatch");
        }
        ContentCachingRequestWrapper request = Terminal.getRequest(ContentCachingRequestWrapper.class);
        byte[] bytes = request.getMethod().equals("GET")?readParam(request):readBody(request);
        if (bytes==null || bytes.length==0){
            throw new SignException("sign mismatch",sign,null);
        }
        String encode = md5.encode(new String(bytes));
        if (!sign.equals(encode)){
            throw new SignException("sign mismatch",sign,encode);
        }
    }


    /**
     * 读取param数据
     * @param request
     * @return
     */
    private byte[] readParam(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.isEmpty()){
            return null;
        }
        StringBuilder requesParams = new StringBuilder();
        TreeMap<String, String[]> params = Maps.newTreeMap();
        params.putAll(parameterMap);
        params.forEach((k,v)->{
            requesParams.append(k).append("=");
            for (String s : v) {
                requesParams.append(s);
            }
        });
        return requesParams.toString().getBytes();
    }


    /**
     * 读取body请求数据
     * @param request
     * @return
     */
    private byte[] readBody(ContentCachingRequestWrapper request) {
        byte[] s = null;
        long contentLengthLong = request.getContentLengthLong();
        if (contentLengthLong==0){
            return null;
        }
        try {
            s = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {}
        if (s==null || s.length==0){
            s = request.getContentAsByteArray();
        }
       return s;
    }
}
