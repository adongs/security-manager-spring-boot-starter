package com.adongs.implement.captcha.processor;

import com.adongs.config.CaptchaGlobalConfig;
import com.adongs.implement.captcha.model.CaptchaConfig;
import com.adongs.implement.captcha.cache.CaptchaCache;
import com.adongs.implement.captcha.controller.CaptchaHandler;
import com.google.common.collect.Maps;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 图片验证码处理器
 * 收到注解采集器(CaptchaCollector)的事件后开始装载,相关数据,
 * 并注入CaptchaEndpointHandlerMapping 到spring工厂中
 * @author yudong
 * @version 1.0
 */
@Component
public class CaptchaProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaProcessor.class);

    @Autowired
    private CaptchaCache captchaCache;
    @Autowired
    private  CaptchaGlobalConfig config;
    @Autowired
    private ApplicationContext context;

    private final Map<String, CaptchaConfig> CAPTCHA_CONFIG =  Maps.newHashMap();


    /**
     * 添加配置信息
     * @param configMap 配置信息
     */
    protected void addConfig(Map<String,CaptchaConfig> configMap){
        if (configMap.isEmpty()){return;}
        CAPTCHA_CONFIG.putAll(configMap);
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(configMap.keySet().toArray(new String[]{}))
                .methods(RequestMethod.GET)
                .build();
        CaptchaHandler captchaHandler = new CaptchaHandler(this);
        try {
            final Map<String, RequestMappingInfoHandlerMapping> beansOfType = context.getBeansOfType(RequestMappingInfoHandlerMapping.class);
            final RequestMappingInfoHandlerMapping handlerMapping = beansOfType.get("requestMappingHandlerMapping");
            final Method links = captchaHandler.getClass().getDeclaredMethod("links", HttpServletRequest.class, HttpServletResponse.class);
            handlerMapping.registerMapping(requestMappingInfo,captchaHandler,links);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建图片二维码,并返回
     * @param request 请求体
     * @param response 响应体
     */
    public void captcha(HttpServletRequest request, HttpServletResponse response){
        final String requestURI = request.getRequestURI();
        final CaptchaConfig captchaConfig = CAPTCHA_CONFIG.get(requestURI);
        String id = request.getHeader(captchaConfig.getRequestId());
        if (StringUtils.isEmpty(id)){
            id = request.getParameter(captchaConfig.getRequestId());
        }
        Captcha captcha = captcha(captchaConfig);
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException ioException) {
            LOGGER.error(ioException.getMessage(),ioException);
        }
        final String text = captcha.text();
        captchaCache.save(id,text,config.getCacheTime());
    }

    /**
     * 验证码图片验证码的结果
     * @param id id
     * @param result 结果
     * @return 验证码图片验证码的结果
     */
    public boolean check(String id,String result){
       return captchaCache.ver(id, result);
    }

    /**
     * 创建图片验证码
     * @param captchaConfig 验证码配置信息
     * @return 验证码
     */
    private Captcha captcha(CaptchaConfig captchaConfig){
        final Captcha captcha = captchaConfig.getCodeType().captcha();
        captcha.setHeight(captchaConfig.getHeight());
        captcha.setWidth(captchaConfig.getWidth());
        captcha.setLen(captchaConfig.getDigits());
        captcha.setCharType(captchaConfig.getCharacterType());
        try {
            captcha.setFont(captchaConfig.getFont());
        } catch (IOException ioException) {
            LOGGER.error(ioException.getMessage(),ioException);
        } catch (FontFormatException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return captcha;
    }

}
