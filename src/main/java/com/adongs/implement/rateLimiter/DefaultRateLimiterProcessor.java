package com.adongs.implement.rateLimiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author yudong
 * @version 1.0
 */
public class DefaultRateLimiterProcessor implements RateLimiterProcessor{

    private final RateLimiter rateLimiter;


    public DefaultRateLimiterProcessor(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public DefaultRateLimiterProcessor(double permitsPerSecond) {
        this.rateLimiter = RateLimiter.create(permitsPerSecond);
    }
    public DefaultRateLimiterProcessor() {
       this(100);
    }


    @Override
    public boolean acquire(String name,int permits) {
        boolean b = rateLimiter.tryAcquire(permits, TimeUnit.MILLISECONDS);
        return b;
    }
}
