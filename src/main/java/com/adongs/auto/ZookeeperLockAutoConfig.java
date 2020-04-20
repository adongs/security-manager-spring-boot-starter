package com.adongs.auto;

import com.adongs.implement.lock.ZookeeperLockAspect;
import com.adongs.implement.lock.processor.ZookeeperProcessor;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 分布式互斥锁(集群Zookeeper)
 * @author yudong
 * @version 1.0
 */
@ConditionalOnClass({CuratorFramework.class})
@ConditionalOnProperty(name = "spring.security.manager.zookeeper.enabled",havingValue = "true",matchIfMissing = true)
public class ZookeeperLockAutoConfig {

    /**
     * 当使用的是 spring-boot-starter-data-redis
     * @param curatorFramework redis模板
     * @return 返回lock处理器
     */
    @Bean
    public ZookeeperProcessor zookeeperProcessor(CuratorFramework curatorFramework){
        return new ZookeeperProcessor(curatorFramework);
    }

    @Bean
    public ZookeeperLockAspect zookeeperLockAspect(){
        return new ZookeeperLockAspect();
    }
}
