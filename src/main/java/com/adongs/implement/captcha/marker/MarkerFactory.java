package com.adongs.implement.captcha.marker;

import com.adongs.config.CaptchaGlobalConfig;
import com.adongs.implement.captcha.cache.CaptchaCache;

import javax.servlet.http.HttpServletRequest;

/**
 * 标记工厂
 * @author yudong
 * @version 1.0
 */
public class MarkerFactory implements Marker{

    private final CaptchaCache captchaCache;
    private final CaptchaGlobalConfig config;
    private final ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal();

    public MarkerFactory(CaptchaCache captchaCache, CaptchaGlobalConfig config) {
        this.captchaCache = captchaCache;
        this.config = config;
    }

    @Override
    public void marker(String id, HttpServletRequest request) {
        String key = id+request.getRequestURI();
        final HttpServletRequest httpServletRequest = threadLocal.get();
        if (httpServletRequest==null || !httpServletRequest.equals(request)) {
            captchaCache.addMarkerCount(key);
            threadLocal.remove();
            threadLocal.set(request);
        }
    }

    @Override
    public void remove(String id, HttpServletRequest request) {
        String key = id+request.getRequestURI();
        captchaCache.clearMark(key);
    }

    @Override
    public boolean threshold(String id, HttpServletRequest request) {
        String key = id+request.getRequestURI();
        final int count = captchaCache.markerCount(key);
        if (count>=config.getThreshold()){
            return true;
        }
        return false;
    }

}
