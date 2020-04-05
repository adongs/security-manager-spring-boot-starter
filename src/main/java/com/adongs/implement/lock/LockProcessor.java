package com.adongs.implement.lock;

/**
 * @author yudong
 * @version 1.0
 * @date 2020/4/5 5:00 下午
 * @modified By
 */
public interface LockProcessor {

    /**
     * 加锁
     * @param key 指定key
     * @param timeout 过期时间 -1表示永久
     * @return 如果成功返回true 失败返回false
     */
    public boolean lock(String key ,long timeout);


    /**
     * 解锁
     * @param key 指定key
     * @return 如果成功返回true 失败返回false
     */
    public boolean unlock(String key);
}
