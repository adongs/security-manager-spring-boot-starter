package com.adongs.implement.core;

import com.adongs.config.ResubmitConfig;
import com.adongs.constant.RefreshTime;
import com.adongs.exception.ResubmitException;
import com.adongs.implement.AbstractContextAspect;
import com.adongs.implement.core.resources.ResourcesProcessor;
import com.adongs.implement.resubmit.ResubmitProcessor;
import com.adongs.annotation.core.Resubmit;
import com.adongs.implement.BaseAspect;
import com.adongs.session.terminal.Terminal;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * 防止重复提交
 */
@Aspect
@Order(2)
public class ResubmitAspect extends AbstractContextAspect<ResubmitProcessor> {

    @Autowired
    private ResubmitConfig resubmitConfig;
    private final static String HEADER_NAME= "headers";

    public ResubmitAspect(ApplicationContext applicationContext) {
        super(applicationContext, ResubmitProcessor.class);
    }


    /**
     * 防止重复提交
     * @param joinPoint JoinPoint
     * @param resubmit Resubmit注解
     */
    @Before(value = "@annotation(resubmit)")
    public void before(JoinPoint joinPoint, Resubmit resubmit){
        final ResubmitProcessor processor = processor(resubmit.processor(), resubmitConfig.getProcessor());
        Map<String, Object> params = getParams(joinPoint);
        Map<String, Object> paramsHeader = Maps.newHashMap();
        addHeaders(paramsHeader);
        paramsHeader.putAll(params);
        String userId = processor.userId(resubmit.userId(), paramsHeader);
        String uid = userId+processor.key(resubmit.uid(),params);
        boolean presence = processor.isPresence(uid);
        if (presence){
            if (refresh(resubmit.refresh())){
                processor.update(uid,time(resubmit.time()));
            }
            throw new ResubmitException("No duplicate submissions allowed");
        }else{
            processor.saveKey(uid,time(resubmit.time()));
        }
    }


    /**
     * 刷新时间
     * @param time 时间
     * @return 刷新时间
     */
    private long time(long time){
        if (time==0){
            return resubmitConfig.getTime();
        }
        if (time==-1){
            return -1;
        }
        return time;
    }

    /**
     * 获取是否刷新
     * @param refreshTime 刷新时间
     * @return 是否刷新
     */
    private boolean refresh(RefreshTime refreshTime){
        switch (refreshTime){
            case TRUE:
                return true;
            case FALSE:
                return false;
        }
        return resubmitConfig.isRefresh();
    }

    /**
     * 添加header信息
     * @param param 容器
     */
    private void addHeaders(Map<String, Object> param){
        HttpServletRequest request = Terminal.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String,String> headerMap = Maps.newHashMap();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            headerMap.put(name,request.getHeader(name));
        }
        param.put(HEADER_NAME,headerMap);
    }
}
