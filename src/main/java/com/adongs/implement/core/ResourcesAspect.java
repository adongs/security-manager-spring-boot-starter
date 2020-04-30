package com.adongs.implement.core;

import com.adongs.annotation.core.Resources;
import com.adongs.constant.Logical;
import com.adongs.exception.ResourcesException;
import com.adongs.implement.BaseAspect;
import com.adongs.implement.core.resources.ResourcesJwtProcessor;
import com.adongs.session.user.Terminal;
import com.google.common.collect.Sets;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 资源认证服务
 */
@Aspect
@Order(2)
public class ResourcesAspect extends BaseAspect {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher(":");

    private final ResourcesJwtProcessor RESOURCES_JWTPROCESSOR;

    private final String RESOURCES;

    public ResourcesAspect(String resources,ResourcesJwtProcessor resourcesJwtProcessor) {
        this.RESOURCES = resources;
        this.RESOURCES_JWTPROCESSOR=resourcesJwtProcessor;
    }

    /**
     * 资源认证服务
     * @param joinPoint JoinPoint
     * @param resources Resources注解
     */
    @Before(value = "@annotation(resources)")
    public void before(JoinPoint joinPoint, Resources resources){
        HttpServletRequest request = Terminal.getRequest();
        String value = request.getHeader(RESOURCES)==null?request.getParameter(RESOURCES):request.getHeader(RESOURCES);
        Optional<String> valueOptional = Optional.ofNullable(value);
        if (!valueOptional.isPresent()){
            throw new ResourcesException("No resources found in header");
        }
        RESOURCES_JWTPROCESSOR.verification(valueOptional.get());
        Set<String> permissions = RESOURCES_JWTPROCESSOR.getPermissions(valueOptional.get());
        Set<String> rules = Sets.newHashSet(resources.permissions());
        if (rules.isEmpty()){
            return;
        }
        Logical logical = resources.plogical();
        if (logical == Logical.OR){
            for (Iterator<String> iterator =permissions.iterator();iterator.hasNext();){
                String next = iterator.next();
                boolean matchOne = matchOne(next, rules);
                if (matchOne){
                    return;
                }
            }
            throw new ResourcesException("No resource permissions");
        }
        if (logical == Logical.AND){
            for (Iterator<String> iterator =permissions.iterator();iterator.hasNext();) {
                String next = iterator.next();
                boolean matchOne = matchOne(next, rules);
                if (!matchOne) {
                    throw new ResourcesException("No resource permissions");
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
        for (Iterator<String> iterator = rules.iterator(); iterator.hasNext();) {
            boolean match = ANT_PATH_MATCHER.matchStart(rule, iterator.next());
            if (match){
                return true;
            }
        }
        return false;
    }
}
