package com.adongs.implement.core;

import com.adongs.annotation.core.Authentication;
import com.adongs.annotation.core.Sightseer;
import com.adongs.constant.Logical;
import com.adongs.implement.BaseAspect;
import com.adongs.session.authenticate.AuthorityAuthentication;
import com.adongs.session.terminal.Terminal;
import com.google.common.collect.Sets;
import com.adongs.exception.AuthorityException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import java.util.Iterator;
import java.util.Set;


/**
 * 授权注解解析
 * 认证当前token
 * 判断是否具有权限
 * 判断是否具有角色
 */
@Aspect
public class AuthenticationAspect extends BaseAspect {

    @Autowired
    private AuthorityAuthentication authorityAuthentication;

    /**
     * 验证方法上的注解
     * @param joinPoint 代理信息
     * @param authentication 注解
     */
    @Before(value = "@annotation(authentication)")
    public void before(JoinPoint joinPoint, Authentication authentication){
        Terminal terminal = Terminal.get();
        authorityAuthentication.authenticate(authentication,terminal);
    }

    /**
     * 验证类上的注解
     * @param joinPoint 代理信息
     * @param authentication 注解
     */
    @Before(value = "@within(authentication)")
    public void beforeClass(JoinPoint joinPoint, Authentication authentication){
        Authentication annotation = getAnnotation(joinPoint, Authentication.class);
        Sightseer sightseer = getAnnotation(joinPoint, Sightseer.class);
        Terminal terminal = Terminal.get();
        if (annotation!=null && sightseer!=null){
            authorityAuthentication.authenticate(authentication,terminal);
        }
    }
}
