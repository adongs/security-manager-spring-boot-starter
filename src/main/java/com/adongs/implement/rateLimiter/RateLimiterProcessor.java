package com.adongs.implement.rateLimiter;

import com.adongs.implement.BaseProcessor;

/**
 * @author yudong
 * @version 1.0
 */
public interface RateLimiterProcessor extends BaseProcessor {

    /**
     * 获取令牌
     * @param permits  等待时间单位毫秒
     * @return 令牌获取成功返回true 否则返回false
     */
    public boolean acquire(long permits);




}
