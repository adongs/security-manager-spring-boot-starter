package com.adongs.auto;

import com.adongs.implement.lock.processor.JedisLockProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 分布式互斥锁(单节点redis)
 * @author yudong
 * @version 1.0
 */
@ConditionalOnClass({RedisTemplate.class})
@ConditionalOnProperty(name = "spring.security.manager.redis.enabled",havingValue = "true",matchIfMissing = true)
public class JedisLockAutoConfig {
    /**
     * 当使用的是 spring-boot-starter-data-redis
     * @param redisTemplate redis模板
     * @return 返回lock处理器
     */
    @Bean
    public JedisLockProcessor jedisLockProcessor(RedisTemplate redisTemplate){
        return new JedisLockProcessor(redisTemplate);
    }
}
