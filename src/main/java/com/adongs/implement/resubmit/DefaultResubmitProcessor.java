package com.adongs.implement.resubmit;

import com.adongs.session.user.Terminal;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.adongs.implement.decrypt.coding.MD5;
import com.adongs.session.user.User;
import com.adongs.utils.el.ElAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yudong
 * @version 1.0
 */
public class DefaultResubmitProcessor implements ResubmitProcessor{

    private static final MD5 md5 = MD5.build();
    private final Cache<String,Long> cache;

    public DefaultResubmitProcessor(Cache<String,Long> cache) {
        this.cache=cache;
    }

    public DefaultResubmitProcessor(int init,long duration, TimeUnit unit) {
        this.cache=CacheBuilder.newBuilder()
                .initialCapacity(init)
                .expireAfterAccess(duration, unit).build();;
    }

    public DefaultResubmitProcessor() {
        this(2048,5,TimeUnit.SECONDS);
    }

    @Override
    public String userId(String el, Map<String, Object> params) {
        if (StringUtils.isEmpty(el)){
            Terminal terminal = Terminal.get();
            if (terminal!=null){
                User user = terminal.getUser();
                return user.getId().toString();
            }
        }else {
            ElAnalysis elAnalysis = new ElAnalysis(params);
            String analysis = elAnalysis.analysis(el, String.class);
            return analysis;
        }
        return null;
    }

    @Override
    public String key(String uid, Map<String, Object> params) {
        if (StringUtils.isEmpty(uid)){
            ContentCachingRequestWrapper request = Terminal.getRequest(ContentCachingRequestWrapper.class);
            uid=new String(request.getMethod().equals("GET")?readParam(request):readBody(request));
            return md5.encode(uid);
        }else{
            ElAnalysis elAnalysis = new ElAnalysis(params);
            String analysis = elAnalysis.analysis(uid, String.class);
            return analysis;
        }
    }

    @Override
    public void saveKey(String key, long time) {
        cache.put(key,time);
    }

    @Override
    public boolean isPresence(String key) {
        Long ifPresent = cache.getIfPresent(key);
        if (ifPresent==null){
            return false;
        }
        if (ifPresent<System.currentTimeMillis()){
            cache.invalidate(key);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void update(String key, long time) {
        saveKey(key,time);
    }


    /**
     * 读取param数据
     * @param request HttpServletRequest
     * @return 返回获取的到的数据
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
     * @param request ContentCachingRequestWrapper
     * @return 获取到的数据
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
