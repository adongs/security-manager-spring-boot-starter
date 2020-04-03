package com.adongs.annotation.extend;

/**
 * 共享锁
 * @author yudong
 * @version 1.0
 */
public @interface Lock {

    /**
     * 锁的唯一标识,支持el表达式
     * @return
     */
    String key() default "";

}
