package com.adongs.implement.core;

import com.adongs.annotation.core.RateLimiters;
import com.adongs.config.RateLimitersConfig;
import com.adongs.exception.RateLimiterException;
import com.adongs.implement.AbstractContextAspect;
import com.adongs.implement.BaseAspect;
import com.adongs.implement.rateLimiter.RateLimiterProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


/**
 * 授权注解解析
 * 认证当前token
 * 判断是否具有权限
 * 判断是否具有角色
 */
@Aspect
public class RateLimiterAspect extends AbstractContextAspect<RateLimiterProcessor> {

    @Autowired
    private RateLimitersConfig config;

    public RateLimiterAspect(ApplicationContext applicationContext) {
        super(applicationContext, RateLimiterProcessor.class);
    }

    @Before(value = "@within(rateLimiter) || @annotation(rateLimiter)")
    public void before(JoinPoint joinPoint, RateLimiters rateLimiter){
        final RateLimiterProcessor processor = processor(rateLimiter.processor(), config.getProcessor());
        final boolean acquire = processor.acquire(permits(rateLimiter));
        if (!acquire){
            throw new RateLimiterException("restriction of visit");
        }
    }

    /**
     * 获取等待时间
     * @param rateLimiter
     * @return
     */
    private long permits(RateLimiters rateLimiter){
        return rateLimiter.permits()==-1?config.getPermits():rateLimiter.permits();
    }



}
