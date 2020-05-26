package com.adongs.implement.captcha.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 图片验证码controller接口
 * @author yudong
 * @version 1.0
 */
@FunctionalInterface
public interface CustomizeHandler {
    Object links(HttpServletRequest request, HttpServletResponse response);
}
