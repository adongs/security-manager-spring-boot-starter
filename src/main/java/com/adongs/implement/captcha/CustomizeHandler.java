package com.adongs.implement.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yudong
 * @version 1.0
 */
@FunctionalInterface
public interface CustomizeHandler {
    Object links(HttpServletRequest request, HttpServletResponse response);
}
