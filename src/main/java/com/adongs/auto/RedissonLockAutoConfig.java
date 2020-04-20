package com.adongs.auto;

import com.adongs.implement.lock.processor.RedissonLockProcessor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 分布式互斥锁(单节点redis)
 * @author yudong
 * @version 1.0
 */
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@ConditionalOnProperty(name = "spring.security.manager.redis.enabled",havingValue = "true",matchIfMissing = true)
@AutoConfigureAfter(name = "RedissonAutoConfiguration")
public class RedissonLockAutoConfig {

    /**
     * 当使用的是 redisson-spring-boot-starter 则配置集群方式
     * @param redissonClient 连接
     * @return 互斥锁对象
     */
    @Bean
    public RedissonLockProcessor redissonLockProcessor(RedissonClient redissonClient){
        return new RedissonLockProcessor(redissonClient);
    }


}
