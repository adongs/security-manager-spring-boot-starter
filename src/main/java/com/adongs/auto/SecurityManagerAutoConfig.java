package com.adongs.auto;

import com.adongs.implement.captcha.CaptchaEndpointHandlerMapping;
import com.adongs.implement.captcha.CaptchaHandler;
import com.adongs.implement.lock.RedisLockAspect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;


/**
 * 自动配置主要类
 */
@Configuration
@ComponentScan("com.adongs")
@Import({EncryptionAutoConfig.class,
        ExcelAutoConfig.class,
        LogOutAutoConfig.class,
        ExcelWebMvcConfigurer.class,
        JedisLockAutoConfig.class,
        RedissonLockAutoConfig.class,
        ZookeeperLockAutoConfig.class})
@AutoConfigureAfter
public class SecurityManagerAutoConfig {

    private final static Log LOGGER = LogFactory.getLog(SecurityManagerAutoConfig.class);

    /**
     * 开启互斥锁
     * @return 开启互斥锁
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.redis.enabled",havingValue = "true")
    public RedisLockAspect redisLockAspect(){
        return new RedisLockAspect();
    }

    @Bean
    public CaptchaEndpointHandlerMapping captchaEndpointHandlerMapping(){
        return new CaptchaEndpointHandlerMapping(new CorsConfiguration(),new CaptchaHandler());
    }

}


