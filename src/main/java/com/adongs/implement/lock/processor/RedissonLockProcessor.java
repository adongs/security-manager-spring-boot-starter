package com.adongs.implement.lock.processor;

import com.adongs.exception.LockException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author yudong
 * @version 1.0
 */
public class RedissonLockProcessor implements LockProcessor<RLock> {

    private RedissonClient redissonClient;

    public RedissonLockProcessor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public RLock lock(String key, long time) {
        RLock lock = redissonClient.getLock(key);
        try {
             lock.tryLock(time, TimeUnit.MILLISECONDS);
            return lock;
        } catch (InterruptedException e) {
            throw new LockException("Unable to obtain lock");
        }
    }

    @Override
    public boolean unlock(RLock lock) {
        lock.unlock();
        return true;
    }
}
