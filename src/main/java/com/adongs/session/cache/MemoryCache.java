package com.adongs.session.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 在内存中进行缓存
 * @author yudong
 * @version 1.0
 */
public class MemoryCache implements Cache{

    private final  com.google.common.cache.Cache<String,Object> cache;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher(":");
    public MemoryCache(long time) {
        cache = CacheBuilder.newBuilder().expireAfterAccess(time, TimeUnit.MILLISECONDS).build();
    }

    @Override
    public void set(Optional<String> key,Optional<Object> value) {
        if (key.isPresent()){
           if (value.isPresent()){
               cache.put(key.get(),value.get());
           }else{
               cache.invalidate(key.get());
           }
        }
    }


    @Override
    public <T> Optional<T> get(Optional<String> key, @NonNull Class<T> clazz) {
        if (key.isPresent()){
            Optional<Object> ifPresent = Optional.ofNullable(cache.getIfPresent(key.get()));
            return ifPresent.isPresent()?Optional.ofNullable((T)ifPresent.get()):Optional.empty();
        }
        return Optional.empty();
    }


    @Override
    public void delete(Optional<String> key) {
        if (key.isPresent()){
            cache.invalidate(key.get());
        }
    }

    @Override
    public void refresh(Optional<String> key) {
        if (key.isPresent()){
            cache.getIfPresent(key.get());
        }
    }


    @Override
    public Optional<Set<String>> keys(Optional<String> key) {
        if (key.isPresent()){
            Set<String> strings = cache.asMap().keySet();
            Set<String> keys = Sets.newHashSet();
            for ( Iterator<String> iterator= strings.iterator();iterator.hasNext();){
                String next = iterator.next();
                boolean match = antPathMatcher.match(next, key.get());
                if (match){
                    keys.add(next);
                }
            }
            return keys.size()>0?Optional.of(keys):Optional.empty();
        }
        return Optional.empty();
    }
}
