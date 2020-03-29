package com.security.manager.implement.rateLimiter;

/**
 * @author yudong
 * @version 1.0
 * @date 2020/3/27 10:49 下午
 * @modified By
 */
public interface RateLimiterProcessor {

    /**
     * 获取令牌
     * @return
     */
    public boolean acquire(String name,int permits);




}
