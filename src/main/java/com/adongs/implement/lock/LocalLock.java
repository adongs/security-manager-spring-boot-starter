package com.adongs.implement.lock;

/**
 * @author yudong
 * @version 1.0
 * @date 2020/4/5 5:27 下午
 * @modified By
 */
public class LocalLock implements LockProcessor {

    @Override
    public boolean lock(String key, long timeout) {
        return false;
    }

    @Override
    public boolean unlock(String key) {
        return false;
    }
}
