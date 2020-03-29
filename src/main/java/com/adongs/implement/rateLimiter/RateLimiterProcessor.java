package com.adongs.implement.rateLimiter;

/**
 * @author yudong
 * @version 1.0
 */
public interface RateLimiterProcessor {

    /**
     * 获取令牌
     * @param name 限流器名称
     * @param permits  等待时间单位毫秒
     * @return 令牌获取成功返回true 否则返回false
     */
    public boolean acquire(String name,int permits);




}
