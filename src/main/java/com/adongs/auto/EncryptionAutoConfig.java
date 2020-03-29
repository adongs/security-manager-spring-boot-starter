package com.adongs.auto;

import com.adongs.implement.decrypt.DefaultDESDecryptProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;

/**
 * @author yudong
 * @version 1.0
 */
public class EncryptionAutoConfig {


    /**
     * 构建des编码
     * @param securityManagerConfig 配置类
     * @return 返回DES加密处理器
     */
    @Bean
    @ConditionalOnExpression("#{environment.getProperty('spring.security.manager.des.key')!=null}")
    public DefaultDESDecryptProcessor des(SecurityManagerConfig securityManagerConfig){
        SecurityManagerConfig.Codings des = securityManagerConfig.getDes();
        if (des.getOffset()!=null){
            return new DefaultDESDecryptProcessor(des.getKey(),des.getOffset());
        }
        return new DefaultDESDecryptProcessor(des.getKey());
    }
}
