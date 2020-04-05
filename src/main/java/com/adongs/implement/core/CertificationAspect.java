package com.adongs.implement.core;

import com.adongs.annotation.core.Certification;
import com.adongs.annotation.core.Sightseer;
import com.adongs.implement.BaseAspect;
import com.adongs.session.user.Terminal;
import com.google.common.collect.Sets;
import com.adongs.exception.AuthorityException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

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


    @Before(value = "@annotation(certification)")
    public void before(JoinPoint joinPoint, Certification certification){
        Terminal terminal = Terminal.get();
        if (terminal==null){
            throw new AuthorityException("permission denied");
        }
        if (terminal.getUser()==null){
            throw new AuthorityException("permission denied");
        }
        Certification.Logical rlogical = certification.rlogical();
        ruleMatch(rlogical,terminal.getRoles(),certification.roles());
        Certification.Logical plogical = certification.plogical();
        ruleMatch(plogical,terminal.getPermissions(),certification.permissions());
    }

    @Before(value = "@within(certification)")
    public void beforeClass(JoinPoint joinPoint, Certification certification){
        Certification annotation = getAnnotation(joinPoint, Certification.class);
        Sightseer sightseer = getAnnotation(joinPoint, Sightseer.class);
        if (annotation==null){
            if(sightseer!=null){
                return;
            }
            Terminal terminal = Terminal.get();
            Certification.Logical rlogical = certification.rlogical();
            ruleMatch(rlogical,terminal.getRoles(),certification.roles());
            Certification.Logical plogical = certification.plogical();
            ruleMatch(plogical,terminal.getPermissions(),certification.permissions());
        }
    }



    /**
     * 规则匹配
     * @param logical 匹配规则
     * @param rawDataSet 用户规则
     * @param nowData 访问需要的规则
     */
    private void ruleMatch(Certification.Logical logical,Set<String> rawDataSet,String[] nowData){
        if (nowData==null || nowData.length==0){
            return;
        }
        if (rawDataSet==null || rawDataSet.size()==0){
            throw new AuthorityException("permission denied");
        }
        Set<String> nowDataSet = Sets.newHashSet(nowData);
        if (logical == Certification.Logical.OR){
            for (Iterator<String> iterator = rawDataSet.iterator();iterator.hasNext();) {
                String next = iterator.next();
                boolean contains = nowDataSet.contains(next);
                if (contains){
                    return;
                }
            }
            throw new AuthorityException("permission denied");
        }
        if (logical == Certification.Logical.AND){
            for (Iterator<String> iterator = nowDataSet.iterator();iterator.hasNext();) {
                String next = iterator.next();
                boolean contains = rawDataSet.contains(next);
                if (!contains){
                    throw new AuthorityException("permission denied");
                }
            }
        }
    }

}
