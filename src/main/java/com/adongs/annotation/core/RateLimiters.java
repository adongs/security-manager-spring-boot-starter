package com.adongs.annotation.core;

import com.adongs.implement.rateLimiter.DefaultRateLimiterProcessor;
import com.adongs.implement.rateLimiter.RateLimiterProcessor;

import java.lang.annotation.*;

/**
 * 限流
 * 通过guava桶令牌方式实现
 * @author yudong
 * @version 1.0
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiters {

    /**
     * 限流器名称
     * 默认global(全局)限流器
     * @return 限流器名称
     */
   String name() default "global";

    /**
     * 获取令牌等待时间单位毫秒
     * @return 获取令牌等待时间单位毫秒
     */
   int permits() default 0;


    /**
     * 处理类
     * @return 处理类
     */
    Class<? extends RateLimiterProcessor> value() default DefaultRateLimiterProcessor.class;
}
