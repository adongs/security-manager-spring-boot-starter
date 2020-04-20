package com.adongs.implement.lock.processor;

/**
 * redis 连接器负责包装redis连接包装
 * @author yudong
 * @version 1.0
 */
public interface LockProcessor<L> {

    /**
     * 加锁
     * @param key 键值
     * @param time 锁定时间
     * @return 加锁对象
     */
    public L lock(String key,long time);

    /**
     * 解锁
     * @param lock 锁对象
     * @return 加锁对象
     */
    public boolean unlock(L lock);

}
