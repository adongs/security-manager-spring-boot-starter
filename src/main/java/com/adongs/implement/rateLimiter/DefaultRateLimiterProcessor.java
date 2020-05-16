package com.adongs.implement.rateLimiter;

import com.adongs.config.RateLimitersConfig;
import com.adongs.constant.CurrentLimitingAlgorithm;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 默认限流实现
 * @author yudong
 * @version 1.0
 */
public class DefaultRateLimiterProcessor implements RateLimiterProcessor{
    private  RateLimiter rateLimiter;

    @Autowired
    private RateLimitersConfig config;

    @PostConstruct
    private void init(){
        final CurrentLimitingAlgorithm arithmetic = config.getArithmetic();
        if (arithmetic==CurrentLimitingAlgorithm.BURSTY){
            this.rateLimiter=RateLimiter.create(config.getPermitsPerSecond());
        }
        if (arithmetic==CurrentLimitingAlgorithm.WARMUP){
            this.rateLimiter=RateLimiter.create(config.getPermitsPerSecond(),config.getWarmupperiod(),TimeUnit.MILLISECONDS);
        }
    }


    @Override
    public boolean acquire(long permits) {
        boolean b = rateLimiter.tryAcquire(permits, TimeUnit.MILLISECONDS);
        return b;
    }

    @Override
    public String name() {
        return "default";
    }
}
