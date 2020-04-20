package com.adongs.implement.lock.processor;


import com.adongs.exception.LockException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;

import java.util.concurrent.TimeUnit;

/**
 * 通过zookeeper实现分布式锁
 * @author yudong
 * @version 1.0
 */
public class ZookeeperProcessor implements LockProcessor<InterProcessSemaphoreMutex>{

    private final static String ROOT_PATH = "/root/manager/lock/";

    private CuratorFramework curatorFramework;

    public ZookeeperProcessor(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @Override
    public InterProcessSemaphoreMutex lock(String key, long time) {
        InterProcessSemaphoreMutex interProcessSemaphoreMutex = new InterProcessSemaphoreMutex(this.curatorFramework,ROOT_PATH+key);
        try {
            boolean acquire = interProcessSemaphoreMutex.acquire(1, TimeUnit.MICROSECONDS);
            if (!acquire){
                throw new LockException("Unable to obtain lock");
            }
            return interProcessSemaphoreMutex;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LockException("Unable to obtain lock");
        }
    }

    @Override
    public boolean unlock(InterProcessSemaphoreMutex lock) {
        try {
            lock.release();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
