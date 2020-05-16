package com.adongs.implement.resubmit;

import com.adongs.annotation.core.Resubmit;
import com.adongs.config.ResubmitConfig;
import com.adongs.session.terminal.Terminal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.adongs.implement.decrypt.coding.MD5;
import com.adongs.utils.el.ElAnalysis;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 默认防止重复
 * @author yudong
 * @version 1.0
 */
public class DefaultResubmitProcessor implements ResubmitProcessor, ApplicationListener<ApplicationStartedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultResubmitProcessor.class);

    private static final MD5 md5 = MD5.singleBuild();
    private final static String OFF= "off";
    @Autowired
    private ResubmitConfig config;
    @Autowired
    private ObjectMapper mapper;

    private Cache<String,Long> cache;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        final ConfigurableApplicationContext context = event.getApplicationContext();
        Set<Long> longTime = Sets.newHashSet();
        Map<String, Object> controller = context.getBeansWithAnnotation(Controller.class);
        Map<String, Object> restController = context.getBeansWithAnnotation(RestController.class);
        controller.putAll(restController);
        final Collection<Object> values = controller.values();
        for (Object value : values) {
            final Method[] methods = value.getClass().getMethods();
            for (Method method : methods) {
                final Resubmit annotation = method.getAnnotation(Resubmit.class);
                if (annotation==null){continue;}
                longTime.add(annotation.time());
            }
        }
        longTime.add(config.getTime());
        final Long maxTime = longTime.stream().max(Long::compare).get();
        this.cache=CacheBuilder.newBuilder()
                .initialCapacity(Integer.MAX_VALUE)
                .expireAfterAccess(maxTime*2, TimeUnit.MILLISECONDS).build();
    }


    @Override
    public String name() {
        return "default";
    }

    @Override
    public String userId(String el, Map<String, Object> params) {
        if (el.equals(OFF)){
            return "";
        }
        if (StringUtils.isEmpty(el)){
            Terminal terminal = Terminal.get();
            return terminal.token().getToken();
        }else {
            ElAnalysis elAnalysis = new ElAnalysis(params);
            String analysis = elAnalysis.analysis(el, String.class);
            return analysis;
        }
    }

    @Override
    public String key(String uid, Map<String, Object> params) {
        if (StringUtils.isEmpty(uid)){
            final HttpServletRequest request = Terminal.getRequest();
            if (request instanceof ContentCachingRequestWrapper){
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper)request;
                return read(wrapper);
            }else{
                return read(params);
            }
        }else{
            ElAnalysis elAnalysis = new ElAnalysis(params);
            String analysis = elAnalysis.analysis(uid, String.class);
            return analysis;
        }
    }


    @Override
    public void saveKey(String key, long time) {
        cache.put(key,System.currentTimeMillis()+time);
    }

    @Override
    public boolean isPresence(String key) {
        Long ifPresent = cache.getIfPresent(key);
        if (ifPresent==null){
            return false;
        }
        if (ifPresent<System.currentTimeMillis()){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void update(String key, long time) {
        saveKey(key,System.currentTimeMillis()+time);
    }


    /**
     * 获取请求数据摘要
     * @param request 请求
     * @return 数据摘要
     */
    private String read(ContentCachingRequestWrapper request){
        final Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder parameterStr = new StringBuilder();
        parameterMap.forEach((k,v)->parameterStr.append(k).append("=").append(StringUtils.join(v)));
        final byte[] contentAsByteArray = request.getContentAsByteArray();
        parameterStr.append(new String(contentAsByteArray));
        return md5.decode(contentAsByteArray.toString());
    }

    /**
     * 获取参数摘要
     * @param params 参数
     * @return 数据摘要
     */
    private String read(Map<String, Object> params){
        StringBuilder parameterStr = new StringBuilder();
        params.forEach((k,v)->{
            parameterStr.append(k).append("=");
            try {
                mapper.writeValueAsString(v);
            }catch (JsonProcessingException e){
                LOGGER.error("数据解析失败 key={}  value=",k,v.getClass().getName(),e);
            }
        });
        return md5.decode(parameterStr.toString());
    }



}
