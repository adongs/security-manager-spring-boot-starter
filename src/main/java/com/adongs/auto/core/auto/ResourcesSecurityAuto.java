package com.adongs.auto.core.auto;

import com.adongs.implement.core.ResourcesAspect;
import com.adongs.implement.core.resources.DefaultResourcesProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 资源验证自动配置
 * @author yudong
 * @version 1.0
 */
public class ResourcesSecurityAuto {


    /**
     * 配置默认的处理器
     * @return 默认的处理器
     */
    @Bean
    public DefaultResourcesProcessor defaultResourcesProcessor(){
        return new DefaultResourcesProcessor();
    }

    /**
     * 配置资源注解
     * @param applicationContext 上下文
     * @return 资源注解
     */
    @Bean
    public ResourcesAspect resourcesAspect(ApplicationContext applicationContext){
        return new ResourcesAspect(applicationContext);
    }



}
