package com.adongs.implement.captcha.marker;

import javax.servlet.http.HttpServletRequest;

/**
 * 标记触发器
 * 当达到要求后进行表达,并统计次数
 * @author yudong
 * @version 1.0
 */
public interface Marker {

    /**
     * 标记
     * @param id id
     * @param request 请求体
     */
    public void marker(String id, HttpServletRequest request);


    /**
     * 清除标记
     * @param id id
     * @param request 请求体
     */
    public void remove(String id, HttpServletRequest request);

    /**
     * 是否达到阈值
     * @param id id
     * @param request 请求体
     * @return true 达到   false未达到
     */
    public boolean threshold(String id, HttpServletRequest request);

}
