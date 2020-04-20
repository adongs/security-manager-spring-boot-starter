package com.adongs.implement.lock.processor;

import com.adongs.exception.LockException;
import com.google.common.collect.Lists;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.UUID;

/**
 * @author yudong
 * @version 1.0
 */
public class JedisLockProcessor implements LockProcessor<JedisLockProcessor.JLock> {
    private RedisTemplate redisTemplate;

    private final static DefaultRedisScript<Long> LOCK = new DefaultRedisScript<Long>(
            "if redis.call(\"setnx\", KEYS[1],KEYS[2]) == 1 then return redis.call(\"pexpire\", KEYS[1], ARGV[1]) else return 0 end"
    ,Long.class);

    private final static DefaultRedisScript<Long> UN_LOCK = new DefaultRedisScript<Long>(
            "if redis.call(\"get\", KEYS[1]) == KEYS[2] then return redis.call(\"del\", KEYS[1]) else return -1 end"
    ,Long.class);

    public JedisLockProcessor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public JLock lock(String key, long time) {
        String uuid = UUID.randomUUID().toString();
        JLock jLock = new JLock(key,uuid);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Long result  = (Long)redisTemplate.execute(LOCK,stringRedisSerializer,stringRedisSerializer, Lists.newArrayList(key,uuid),String.valueOf(time));
        if (result!=null && result == 1){
            return jLock;
        }
        throw new LockException("Unable to obtain lock");
    }

    @Override
    public boolean unlock(JLock lock) {
        List<String> param = Lists.newArrayList(lock.getLock(),lock.getKey());
        Object result = redisTemplate.execute(UN_LOCK, param);
        if (result!=null && (Long)result == 1){
            return true;
        }
        return false;
    }


    public static class JLock{
        private final String lock;
        private final String key;

     public JLock(String lock, String key) {
         this.lock = lock;
         this.key = key;
     }

     public String getLock() {
         return lock;
     }

     public String getKey() {
         return key;
     }
 }
}
