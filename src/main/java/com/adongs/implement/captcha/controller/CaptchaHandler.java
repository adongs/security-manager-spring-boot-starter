package com.adongs.implement.captcha.controller;

import com.adongs.implement.captcha.processor.CaptchaProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 图片验证码controller
 * @author yudong
 * @version 1.0
 */
public class CaptchaHandler implements CustomizeHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizeHandler.class);

    private final CaptchaProcessor captchaCache;

    public CaptchaHandler(CaptchaProcessor captchaCache) {
        this.captchaCache = captchaCache;
    }

    @Override
    public Object links(HttpServletRequest request, HttpServletResponse response) {
        captchaCache.captcha(request,response);
        return null;
    }



}
