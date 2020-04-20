package com.adongs.auto;


import com.adongs.implement.lock.processor.JedisLockProcessor;
import com.adongs.implement.lock.processor.RedissonLockProcessor;
import com.adongs.implement.lock.processor.ZookeeperProcessor;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 共享锁配置
 * @author yudong
 * @version 1.0
 */
public class LockAutoConfig {




    @ConditionalOnClass(name = "RedissonClient")
    public class RedissonLockAutoConfig{
        /**
         * 当使用的是 redisson
         * @param redissonClient redis连接
         * @return 返回lock处理器
         */
        @ConditionalOnBean(type = "RedissonClient")
        public RedissonLockProcessor redissonLockProcessor(RedissonClient redissonClient){
            return new RedissonLockProcessor(redissonClient);
        }
  }
    @ConditionalOnClass(name = "CuratorFramework")
    public class ZookeeperLockAutoConfig{
        /**
         * 当使用的是 zookeeper
         * @param curatorFramework zookeeper
         * @return 返回lock处理器
         */
        @ConditionalOnBean(type = "CuratorFramework")
        public ZookeeperProcessor zookeeperProcessor(CuratorFramework curatorFramework){
            return new ZookeeperProcessor(curatorFramework);
        }
   }

}
