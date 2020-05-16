package com.adongs.session.authenticate;

import com.adongs.annotation.core.Authentication;
import com.adongs.constant.Logical;
import com.adongs.exception.AuthorityException;
import com.adongs.exception.TokenException;
import com.adongs.exception.UserAuthenticationException;
import com.adongs.session.aggregator.AuthorityAggregator;
import com.adongs.session.aggregator.UrlAuthority;
import com.adongs.session.cache.CacheManager;
import com.adongs.session.terminal.AbstractTerminal;
import com.google.common.collect.Sets;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * 用户权限认证
 * @author yudong
 * @version 1.0
 */
public class AuthorityAuthentication {

    private final static AntPathMatcher ANTPATHMATCHER = new AntPathMatcher(":");

    private final AuthorityAggregator authorityAggregator;

    private final CacheManager cacheManager;

    public AuthorityAuthentication(AuthorityAggregator authorityAggregator, CacheManager cacheManager) {
        this.authorityAggregator = authorityAggregator;
        this.cacheManager = cacheManager;
    }

    /**
     * 判断用户是否有权限
     * @param httpServletRequest 请求体
     * @param terminal 终端信息
     * @throws AuthorityException 权限异常
     */
    public void authenticate(HttpServletRequest httpServletRequest, AbstractTerminal terminal)throws AuthorityException{
        final String requestURI = httpServletRequest.getRequestURI();
        final boolean ignore = authorityAggregator.ignore(requestURI);
        if (!ignore){
            final UrlAuthority urlAuthority = authorityAggregator.authorityInfo(requestURI);
            ruleMatch(urlAuthority.getRolesLogical(),terminal.role(),urlAuthority.getRoles());
            ruleMatch(urlAuthority.getAuthorityLogical(),terminal.authority(),urlAuthority.getAuthority());
        }
    }

    /**
     * 判断用户是否有权限
     * @param authentication 权限注解
     * @param terminal 终端信息
     * @throws AuthorityException 权限异常
     */
    public void authenticate(Authentication authentication, AbstractTerminal terminal)throws AuthorityException{
        final Logical rlogical = authentication.rlogical();
        final Logical plogical = authentication.plogical();
        final String[] roles = authentication.roles();
        final String[] permissions = authentication.permissions();
        final Object user = terminal.getUser();
        if (user==null){
            userAuthentication(terminal);
        }
        ruleMatch(rlogical,terminal.role(),Sets.newHashSet(roles));
        ruleMatch(plogical,terminal.authority(),Sets.newHashSet(permissions));

    }

    /**
     * 认证用户token是否合法并添加用户信息和权限
     * @param terminal 终端信息
     */
    private void userAuthentication(AbstractTerminal terminal){
        if (terminal.token()==null){
            throw new TokenException("token invalid",null);
        }
        final Optional<Serializable> user = cacheManager.getUser(Optional.of(terminal.token().getToken()));
        if (!user.isPresent()){
            throw new UserAuthenticationException("User authentication failed",terminal.token().getToken());
        }
        terminal.addUser(user.get());
        final Optional<Set<String>> authority = cacheManager.getAuthority(Optional.ofNullable(terminal.token().getToken()));
        terminal.addAuthority(authority.isPresent()?authority.get():Sets.newHashSet());
        final Optional<Set<String>> roles = cacheManager.getRole(Optional.ofNullable(terminal.token().getToken()));
        terminal.addRoles(roles.isPresent()?roles.get():Sets.newHashSet());
    }

    /**
     * 认证用户token是否合法并添加用户信息和权限
     * @param httpServletRequest 请求体
     * @param terminal 终端信息
     */
    public void userAuthentication(HttpServletRequest httpServletRequest,AbstractTerminal terminal){
        final String requestURI = httpServletRequest.getRequestURI();
        final boolean ignore = authorityAggregator.ignore(requestURI);
        if (ignore){
            return;
        }
        userAuthentication(terminal);
    }




    /**
     * 规则匹配
     * @param logical 访问匹配规则关系
     * @param userRule 用户规则
     * @param apiRule 访问需要的规则
     */
    private void ruleMatch(Logical logical, Set<String> userRule, Set<String> apiRule){
         if (apiRule.isEmpty()){
             return;
         }
         if (logical == Logical.OR){
             for (Iterator<String> iterator = apiRule.iterator(); iterator.hasNext();) {
                 final boolean matchOne = matchOne(iterator.next(), userRule);
                 if (matchOne){
                     return;
                 }
             }
             throw new AuthorityException("permission denied");
         }
        if (logical == Logical.AND) {
            for (Iterator<String> iterator = apiRule.iterator(); iterator.hasNext(); ) {
                final boolean matchOne = matchOne(iterator.next(), userRule);
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
            boolean match = ANTPATHMATCHER.matchStart(rule, iterator.next());
            if (match){
                return true;
            }
        }
        return false;
    }


}
