package com.adongs.implement.core;

import com.adongs.annotation.core.RateLimiters;
import com.adongs.exception.RateLimiterException;
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
public class RateLimiterAspect extends BaseAspect {

    @Autowired
    private ApplicationContext applicationContext;

    @Before(value = "@within(rateLimiter) || @annotation(rateLimiter)")
    public void before(JoinPoint joinPoint, RateLimiters rateLimiter){
        RateLimiterProcessor bean = applicationContext.getBean(rateLimiter.value());
        if (bean == null){
            return;
        }
        boolean acquire = bean.acquire(rateLimiter.name(), rateLimiter.permits());
        if (!acquire){
            throw new RateLimiterException("restriction of visit");
        }
    }
}
