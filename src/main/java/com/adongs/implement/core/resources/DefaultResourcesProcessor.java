package com.adongs.implement.core.resources;

import com.adongs.config.ResourceCertificationConfig;
import com.adongs.exception.ResourcesException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.Set;

/**
 * 默认jwt验证实现
 * @author yudong
 * @version 1.0
 */
public class DefaultResourcesProcessor implements ResourcesProcessor<String> {

    private final static String AUTHORITY_KEY="authority";

    @Autowired
    private  ResourceCertificationConfig config;

    /**
     * 创建jwt
     * @param issuer 签发者 不允许为空
     * @param sub 面向的用户 允许为空
     * @param aud 接收jwt的一方 允许为空
     * @param expiresAt 过期时间 允许为空
     * @param notBefore 在什么时间之前，该jwt都是不可用的 允许为空
     * @param jwtId  jwt的唯一身份标识 允许为空
     * @param authority 拥有的权限 允许为空
     * @return jwt字符串
     */
    @Override
    public String create(@NonNull String issuer, String sub, String aud, Date expiresAt, Date notBefore, String jwtId, String ... authority) {
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        if (!StringUtils.isEmpty(sub)){
            builder.withSubject(sub);
        }
        if (StringUtils.isEmpty(aud)){
            builder.withAudience(aud);
        }
        if (expiresAt!=null){
            builder.withExpiresAt(expiresAt);
        }
        if(notBefore!=null){
            builder.withNotBefore(notBefore);
        }
        if (!StringUtils.isEmpty(jwtId)){
            builder.withJWTId(jwtId);
        }
        if (authority!=null && authority.length>0){
            builder.withArrayClaim(AUTHORITY_KEY,authority);
        }
      return  builder.sign(config.getMode().toAlgorithm(config));
    }

    /**
     * 验证jwt
     * @param jwt 令牌字符串
     */
    @Override
    public void verification(String jwt) throws ResourcesException {
        JWTVerifier require = JWT.require(config.getMode().toAlgorithm(config)).build();
        try {
            require.verify(jwt);
        }catch (JWTVerificationException e){
            throw new ResourcesException(e.getMessage());
        }
    }

    /**
     * 获取权限
     * @param jwt 令牌字符串
     * @return  权限
     * @throws ResourcesException 资源验证异常
     */
    @Override
    public Set<String> permissions(String jwt) throws ResourcesException {
        DecodedJWT decode = JWT.decode(jwt);
        Claim claim = decode.getClaim(AUTHORITY_KEY);
        String[] strings = claim.asArray(String.class);
        return strings==null?Sets.newHashSet():Sets.newHashSet(strings);
    }


    @Override
    public String name() {
        return "default";
    }



    @Override
    public DecodedJWT subject(String jwt) {
        return JWT.decode(jwt);
    }
}
