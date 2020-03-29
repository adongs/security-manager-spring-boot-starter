package com.security.manager.auto;

import com.security.manager.implement.decrypt.DefaultDESDecryptProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;

/**
 * @author yudong
 * @version 1.0
 * @date 2020/3/28 10:05 下午
 * @modified By
 */
public class EncryptionAutoConfig {


    /**
     * 构建des编码
     * @param securityManagerConfig
     * @return
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
