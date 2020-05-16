package com.adongs.session.cache;

import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

/**
 * 缓存
 * @author yudong
 * @version 1.0
 */
public interface Cache {

    /**
     * 缓存键值
     * @param key 键
     * @param value 值
     */
    public void set(Optional<String> key , Optional<Object> value);

    /**
     * 获取
     * @param key 键
     * @param clazz 值类型
     * @param <T> 泛型
     * @return 值
     */
    public <T> Optional<T> get(Optional<String> key,@NonNull Class<T> clazz);

    /**
     * 删除
     * @param key 键
     */
    public void delete(Optional<String> key);

    /**
     * 刷新过期时间
     * @param key 键
     */
    public void refresh(Optional<String> key);

    /**
     * 模糊查询
     * @param key 键
     * @return 值
     */
    public Optional<Set<String>> keys(Optional<String> key);

}
