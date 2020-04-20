package com.adongs.annotation.extend.lock;

import com.adongs.implement.lock.processor.LockProcessor;
import com.adongs.implement.lock.processor.ZookeeperProcessor;

import java.lang.annotation.*;

/**
 * 使用Zookeeper进行锁
 * @author yudong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ZookeeperLock {

    /**
     * 锁的唯一标识,支持el表达式
     * @return  锁的唯一标识,支持el表达式
     */
    String key() default "";

    /**
     * 自定义解析
     * @return 解析器
     */
    Class<? extends LockProcessor> processor() default ZookeeperProcessor.class;
}
