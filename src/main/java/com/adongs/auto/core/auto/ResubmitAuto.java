package com.adongs.auto.core.auto;

import com.adongs.implement.core.ResubmitAspect;
import com.adongs.implement.resubmit.DefaultResubmitProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 重复提交配置
 * @author yudong
 * @version 1.0
 */
public class ResubmitAuto {


    /**
     * 启动重复提交注解
     * @param applicationContext 上线文
     * @return 防止重复提交注解
     */
    @Bean
    public ResubmitAspect resubmitAspect(ApplicationContext applicationContext){
        return new ResubmitAspect(applicationContext);
    }

    /**
     * 构建重复提交的默认处理器
     * @return 处理器
     */
    @Bean
    public DefaultResubmitProcessor defaultResubmitProcessor(){
        return new DefaultResubmitProcessor();
    }


}
