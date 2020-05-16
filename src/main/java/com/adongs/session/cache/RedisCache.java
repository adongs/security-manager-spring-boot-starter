package com.adongs.session.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis进行缓存
 * @author yudong
 * @version 1.0
 */
public class RedisCache implements Cache{

    @Autowired
    private  RedisTemplate redisTemplate;
    private final long time;

    public RedisCache(long time) {
        this.time=time;
    }

    @Override
    public void set(Optional<String> key, Optional<Object> value) {
        if (key.isPresent()){
            if (value.isPresent()){
                Class<?> valueClass = value.get().getClass();
                if (Set.class.isAssignableFrom(valueClass)){
                    Object[] values = ((Collection) value.get()).toArray();
                    redisTemplate.opsForSet().add(key.get(),values);
                    redisTemplate.expire(key,time, TimeUnit.MILLISECONDS);
                    return;
                }
                if (Map.class.isAssignableFrom(valueClass)){
                    redisTemplate.opsForHash().putAll(key.get(),(Map) value.get());
                    redisTemplate.expire(key,time, TimeUnit.MILLISECONDS);
                    return;
                }
                redisTemplate.opsForValue().set(key,value);
            }else{
                redisTemplate.delete(key.get());
            }
        }
    }

    @Override
    public <T> Optional<T> get(Optional<String> key,@NonNull Class<T> clazz) {
        if (key.isPresent()) {
            if (Set.class.isAssignableFrom(clazz)) {
                Set members = redisTemplate.opsForSet().members(key.get());
                return members!=null?Optional.ofNullable((T)members):Optional.empty();
            }
            if (Map.class.isAssignableFrom(clazz)) {
                Map entries = redisTemplate.opsForHash().entries(key);
                return entries!=null?Optional.ofNullable((T)entries):Optional.empty();
            }
            Object object = redisTemplate.opsForValue().get(key);
            return object!=null?Optional.ofNullable((T)object):Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public void delete(Optional<String> key) {
        if (key.isPresent()){
            redisTemplate.delete(key.get());
        }
    }

    @Override
    public void refresh(Optional<String> key) {
        if (key.isPresent()) {
            Boolean hasKey = redisTemplate.hasKey(key.get());
            if (hasKey) {
                redisTemplate.expire(key.get(), time, TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public Optional<Set<String>> keys(Optional<String> key) {
        if (key.isPresent()){
            Set keys = redisTemplate.keys(key.get());
           return Optional.ofNullable(keys);
        }
        return Optional.empty();
    }
}
