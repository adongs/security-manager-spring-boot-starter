package com.adongs.annotation.extend.lock;

/**
 * 使用Zookeeper进行锁
 * @author yudong
 * @version 1.0
 */
public @interface ZookeeperLock {

    /**
     * 锁的唯一标识,支持el表达式
     * @return  锁的唯一标识,支持el表达式
     */
    String key() default "";

    /**
     * 超时时间单位毫秒
     * @return 超时时间单位毫秒
     */
    long timeout() default 10000;
}
