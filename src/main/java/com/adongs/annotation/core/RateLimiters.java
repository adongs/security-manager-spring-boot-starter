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
     * 默认使用配置文件限流
     * @return 限流器名称
     */
   String processor() default "";

    /**
     * 获取令牌等待时间单位毫秒
     * -1表示从配置中获取
     * @return 获取令牌等待时间单位毫秒
     */
   int permits() default -1;


}
