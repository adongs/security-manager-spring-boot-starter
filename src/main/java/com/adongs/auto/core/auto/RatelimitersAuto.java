package com.adongs.auto.core.auto;

import com.adongs.implement.core.RateLimiterAspect;
import com.adongs.implement.rateLimiter.DefaultRateLimiterProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 限流
 * @author yudong
 * @version 1.0
 */
public class RatelimitersAuto {

    /**
     * 限流注解
     * @param applicationContext 上下文
     * @return 注解
     */
    @Bean
    public RateLimiterAspect rateLimiterAspect(ApplicationContext applicationContext){
        return new RateLimiterAspect(applicationContext);
    }

    /**
     * 限流默认处理器
     * @return 限流默认处理器
     */
    @Bean
    public DefaultRateLimiterProcessor defaultRateLimiterProcessor(){
        return new DefaultRateLimiterProcessor();
    }

}
