package com.adongs.implement.lock;

/**
 * @author yudong
 * @version 1.0
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