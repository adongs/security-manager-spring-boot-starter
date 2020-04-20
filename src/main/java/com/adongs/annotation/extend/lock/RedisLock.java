package com.adongs.annotation.extend.lock;

import com.adongs.implement.lock.processor.JedisLockProcessor;
import com.adongs.implement.lock.processor.LockProcessor;
import com.adongs.implement.lock.processor.RedissonLockProcessor;

import java.lang.annotation.*;

/**
 * 通过redis锁
 * @author yudong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisLock {
    /**
     * 锁的唯一标识,支持el表达式
     * @return 锁的唯一标识,支持el表达式
     */
    String key() default "";

    /**
     * 超时时间单位毫秒
     * @return 超时时间单位毫秒
     */
    long timeout() default 10000;

    /**
     * 自定义解析
     * @return 解析器
     */
    Class<? extends LockProcessor> processor() default JedisLockProcessor.class;
}
