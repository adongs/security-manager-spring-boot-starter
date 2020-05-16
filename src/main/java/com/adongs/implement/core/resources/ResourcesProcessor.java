package com.adongs.implement.core.resources;

import com.adongs.exception.ResourcesException;
import com.adongs.implement.BaseProcessor;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Set;

/**
 * jwt创建和验证
 * @author yudong
 * @version 1.0
 */
public interface ResourcesProcessor<T> extends BaseProcessor {


    /**
     * 创建
     * @param issuer 签发者 不允许为空
     * @param sub 面向的用户 允许为空
     * @param aud 接收jwt的一方 允许为空
     * @param expiresAt 过期时间
     * @param notBefore 在什么时间之前，该jwt都是不可用的
     * @param jwtId  jwt的唯一身份标识
     * @param authority 拥有的权限
     * @return jwt字符串
     */
    public String create(T issuer, String sub, T aud, Date expiresAt,Date notBefore,String jwtId,String ... authority);


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
    public Set<String> permissions(String jwt)throws ResourcesException;

    /**
     * 获取jwt解密数据
     * @param jwt 令牌字符串
     * @return 解密数据
     */
    public DecodedJWT subject(String jwt);

}
