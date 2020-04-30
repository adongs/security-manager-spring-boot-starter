package com.adongs.implement.core;

import com.adongs.annotation.core.Authentication;
import com.adongs.annotation.core.Sightseer;
import com.adongs.constant.Logical;
import com.adongs.implement.BaseAspect;
import com.adongs.session.user.Terminal;
import com.google.common.collect.Sets;
import com.adongs.exception.AuthorityException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
public class CertificationAspect extends BaseAspect {

    private AntPathMatcher antPathMatcher = new AntPathMatcher(":");

    @Before(value = "@annotation(authentication)")
    public void before(JoinPoint joinPoint, Authentication authentication){
        Terminal terminal = Terminal.get();
        if (terminal==null){
            throw new AuthorityException("permission denied");
        }
        if (terminal.getUser()==null){
            throw new AuthorityException("permission denied");
        }
        Logical rlogical = authentication.rlogical();
        ruleMatch(rlogical,terminal.getRoles(), authentication.roles());
        Logical plogical = authentication.plogical();
        ruleMatch(plogical,terminal.getPermissions(), authentication.permissions());
    }

    @Before(value = "@within(authentication)")
    public void beforeClass(JoinPoint joinPoint, Authentication authentication){
        Authentication annotation = getAnnotation(joinPoint, Authentication.class);
        Sightseer sightseer = getAnnotation(joinPoint, Sightseer.class);
        if (annotation==null){
            if(sightseer!=null){
                return;
            }
            Terminal terminal = Terminal.get();
            Logical rlogical = authentication.rlogical();
            ruleMatch(rlogical,terminal.getRoles(), authentication.roles());
            Logical plogical = authentication.plogical();
            ruleMatch(plogical,terminal.getPermissions(), authentication.permissions());
        }
    }



    /**
     * 规则匹配
     * @param logical 匹配规则
     * @param rawDataSet 用户规则
     * @param nowData 访问需要的规则
     */
    private void ruleMatch(Logical logical, Set<String> rawDataSet, String[] nowData){
        if (nowData==null || nowData.length==0){
            return;
        }
        if (rawDataSet==null || rawDataSet.size()==0){
            throw new AuthorityException("permission denied");
        }
        Set<String> nowDataSet = Sets.newHashSet(nowData);
        if (logical == Logical.OR){
            for (Iterator<String> iterator = nowDataSet.iterator();iterator.hasNext();) {
                String next = iterator.next();
                boolean matchOne = matchOne(next, rawDataSet);
                if (matchOne){
                    return;
                }
            }
            throw new AuthorityException("permission denied");
        }
        if (logical == Logical.AND){
            for (Iterator<String> iterator = nowDataSet.iterator();iterator.hasNext();) {
                String next = iterator.next();
                boolean matchOne = matchOne(next, rawDataSet);
                if (!matchOne) {
                    throw new AuthorityException("permission denied");
                }
            }
        }
    }


    /**
     * 规则匹配
     * @param rule 原始规则
     * @param rules 现有规则集合
     * @return
     */
    private boolean matchOne(String rule,Set<String> rules){
        for (Iterator<String> iterator = rules.iterator();iterator.hasNext();) {
            boolean match = antPathMatcher.matchStart(rule, iterator.next());
            if (match){
                return true;
            }
        }
        return false;
    }


}
