package com.adongs.implement.core.resources;

import com.adongs.exception.ResourcesException;

import java.util.Date;
import java.util.Set;

/**
 * jwt创建和验证
 * @author yudong
 * @version 1.0
 */
public interface ResourcesJwtProcessor<T> {

    /**
     * 创建
     * @param issuer 签发者 不允许为空
     * @param sub 面向的用户 允许为空
     * @param subject 面向用户 允许为空
     * @param expiresAt 过期时间
     * @param notBefore 在什么时间之前，该jwt都是不可用的
     * @param jwtId  jwt的唯一身份标识
     * @param authority 拥有的权限
     * @return jwt字符串
     */
    public String create(T issuer, String sub, T subject, Date expiresAt,Date notBefore,String jwtId,String ... authority);


    /**
     * 验证jwt
     * @param jwt 令牌字符串
     */
    public void verification(String jwt)throws ResourcesException;


    /**
     * 获取权限
     * @param jwt 令牌字符串
     * @return  权限
     * @throws ResourcesException 资源验证异常
     */
    public Set<String> getPermissions(String jwt)throws ResourcesException;
}
