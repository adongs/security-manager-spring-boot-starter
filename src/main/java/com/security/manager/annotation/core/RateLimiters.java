package com.security.manager.annotation.core;

import com.security.manager.implement.rateLimiter.DefaultRateLimiterProcessor;
import com.security.manager.implement.rateLimiter.RateLimiterProcessor;

import java.lang.annotation.*;

/**
 * 限流
 * 通过guava桶令牌方式实现
 * @author yudong
 * @version 1.0
 * @date 2020/2/28 12:43 下午
 * @modified By
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiters {

    /**
     * 限流器名称
     * 默认global(全局)限流器
     * @return
     */
   String name() default "global";

    /**
     * 获取令牌等待时间单位毫秒
     * @return
     */
   int permits() default 0;


    /**
     * 处理类
     * @return
     */
    Class<? extends RateLimiterProcessor> value() default DefaultRateLimiterProcessor.class;
}
