package com.adongs.auto.core.auto;

import com.adongs.implement.core.SignAspect;
import com.adongs.implement.sign.DefaultSignProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author yudong
 * @version 1.0
 */
public class SignAuto {

    /**
     * 开启盐值校验注解
     * @param applicationContext 上下文
     * @return 盐值校验注解
     */
    @Bean
    public SignAspect signAspect(ApplicationContext applicationContext){
        return new SignAspect(applicationContext);
    }

    /**
     * 配置默认处理器
     * @return 配置默认处理器
     */
    @Bean
    public DefaultSignProcessor defaultSignProcessor(){
        return new DefaultSignProcessor();
    }
}
