package com.adongs.implement.core;

import com.adongs.annotation.core.Resources;
import com.adongs.config.ResourceCertificationConfig;
import com.adongs.constant.Logical;
import com.adongs.exception.ResourcesException;
import com.adongs.implement.AbstractContextAspect;
import com.adongs.implement.BaseAspect;
import com.adongs.implement.core.resources.ResourcesProcessor;
import com.adongs.session.terminal.Terminal;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 资源认证服务
 */
@Aspect
@Order(2)
public class ResourcesAspect extends AbstractContextAspect<ResourcesProcessor>{

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher(":");

    @Autowired
    private ResourceCertificationConfig config;

    public ResourcesAspect(ApplicationContext applicationContext) {
        super(applicationContext, ResourcesProcessor.class);
    }

    /**
     * 资源认证服务
     * @param joinPoint JoinPoint
     * @param resources Resources注解
     */
    @Before(value = "@annotation(resources)")
    public void before(JoinPoint joinPoint, Resources resources){
        final Optional<String> token = token();
        if (!token.isPresent()){
            throw new ResourcesException("No resources found in header");
        }
        final ResourcesProcessor processor = processor(resources.processor(),config.getDefaultProcessor());
        processor.verification(token.get());
        authority(resources,processor.permissions(token.get()));
    }


    /**
     * 权限认证
     * @param resources 资源注解
     * @param tokenRoles 用户权限
     */
   private void authority(Resources resources,Set<String> tokenRoles){
       final Set<String> permissions = Sets.newHashSet(resources.permissions());
       final Logical plogical = resources.plogical();
       if (permissions.isEmpty()){
           return;
       }
       if (plogical == Logical.OR){
           for (Iterator<String> iterator =permissions.iterator();iterator.hasNext();){
               String next = iterator.next();
               boolean matchOne = matchOne(next, tokenRoles);
               if (matchOne){
                   return;
               }
           }
           throw new ResourcesException("No resource permissions");
       }
       if (plogical == Logical.AND){
           for (Iterator<String> iterator =permissions.iterator();iterator.hasNext();) {
               String next = iterator.next();
               boolean matchOne = matchOne(next, tokenRoles);
               if (!matchOne) {
                   throw new ResourcesException("No resource permissions");
               }
           }
       }
   }

    /**
     * 获取令牌
     * @return
     */
    private Optional<String> token(){
        final String resources = config.getResources();
        HttpServletRequest request = Terminal.getRequest();
        final String parameter = request.getParameter(resources);
        if (parameter!=null){
            return Optional.ofNullable(parameter);
        }
        return Optional.ofNullable(request.getHeader(resources));
    }

    /**
     * 规则匹配
     * @param rule 原始规则
     * @param rules 现有规则集合
     * @return
     */
    private boolean matchOne(String rule,Set<String> rules){
        for (Iterator<String> iterator = rules.iterator(); iterator.hasNext();) {
            boolean match = ANT_PATH_MATCHER.matchStart(rule, iterator.next());
            if (match){
                return true;
            }
        }
        return false;
    }
}
