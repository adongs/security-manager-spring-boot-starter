package com.adongs.session.cache;

import com.adongs.session.data.SecurityDataSource;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author yudong
 * @version 1.0
 */
public class CacheManager {
    private final static String PREFIX = "security:manager:spring:cache:";
    private final static String AUTHORITY_PREFIX = PREFIX+"authority:";
    private final static String ROLES_PREFIX = PREFIX+"roles:";
    private final static String USER_PREFIX = PREFIX+"user:";
    private final static String ATTRIBUTE_PREFIX = PREFIX+"attribute:";
    private final static String BLURRY = PREFIX+"*:";
    private final Cache cache;
    private final SecurityDataSource dataSource;

    public CacheManager(Cache cache, SecurityDataSource dataSource) {
        this.cache = cache;
        this.dataSource = dataSource;
    }

    /**
     * 获取权限
     * @param token 用户token
     * @return 权限
     */
    public Optional<Set<String>> getAuthority(Optional<String> token){
        if (token.isPresent()){
            Optional<String> key = Optional.of(AUTHORITY_PREFIX + token.get());
            Optional<Set> set = cache.get(key, Set.class);
            if (set.isPresent()){
                Set<String> authority = set.get();
                return Optional.of(authority);
            }else{
                Optional<Set<String>> permissions = dataSource.permissions(token);
                if(permissions.isPresent()){
                    cache.set(key,Optional.of(permissions.get()));
                    return permissions;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获取角色
     * @param token 用户token
     * @return 角色
     */
    public Optional<Set<String>> getRole(Optional<String> token){
        if (token.isPresent()){
            Optional<String> key = Optional.of(ROLES_PREFIX + token.get());
            Optional<Set> set = cache.get(key, Set.class);
            if (set.isPresent()){
                Set<String> authority = set.get();
                return Optional.of(authority);
            }else{
                Optional<Set<String>> roles = dataSource.roles(token);
                if(roles.isPresent()){
                    cache.set(key,Optional.of(roles.get()));
                    return roles;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获取用户信息
     * @param token 用户token
     * @param <T> 泛型
     * @return 用户信息
     */
    public <T extends Serializable>Optional<T> getUser(Optional<String> token){
        if(token.isPresent()){
            Optional<String> key = Optional.of(USER_PREFIX+token);
            Optional<Serializable> user = cache.get(key, Serializable.class);
            if(user.isPresent()){
                return (Optional<T>) user;
            }else{
                Optional<Serializable> userdb = dataSource.token(token);
                if (userdb.isPresent()){
                    cache.set(key,Optional.of(userdb.get()));
                    return (Optional<T>) userdb;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获取当前会话的变量
     * @param token 用户token
     * @return 前会话的变量
     */
    public Optional<Map> getAttribute(String token){
        Optional<String> key = Optional.of(ATTRIBUTE_PREFIX+token);
        Optional<Map> map = cache.get(key, Map.class);
        return map;
    }


    /**
     * 刷新会话过期时间
     * @param token 用户token
     */
    public void refresh(String token){
        Optional<String> key = Optional.of(BLURRY+token);
        Optional<Set<String>> keys = cache.keys(key);
        if (keys.isPresent()) {
             for (Iterator<String> iterator = keys.get().iterator(); iterator.hasNext(); ) {
                 cache.refresh(Optional.of(iterator.next()));
             }
         }
    }

    /**
     * 删除缓存
     * @param token 用户token
     */
    public void delete(String token){
        Optional<String> key = Optional.of(BLURRY+token);
        Optional<Set<String>> keys = cache.keys(key);
        if (keys.isPresent()) {
            for (Iterator<String> iterator = keys.get().iterator(); iterator.hasNext(); ) {
                cache.delete(Optional.of(iterator.next()));
            }
        }
    }




}
