package com.adongs.auto.core.auto;

import com.adongs.annotation.core.Resubmit;
import com.adongs.config.AuthenticationConfig;
import com.adongs.constant.ResubmitTime;
import com.adongs.implement.core.AuthenticationAspect;
import com.adongs.session.aggregator.AuthorityAggregator;
import com.adongs.session.authenticate.AuthorityAuthentication;
import com.adongs.session.cache.Cache;
import com.adongs.session.cache.CacheManager;
import com.adongs.session.cache.MemoryCache;
import com.adongs.session.cache.RedisCache;
import com.adongs.session.data.DefaultSecurityDataSource;
import com.adongs.session.data.SecurityDataSource;
import com.adongs.session.filter.AuthorityAuthenticationFilter;
import com.adongs.session.filter.TerminalFilter;
import com.adongs.session.filter.UserAuthenticationFilter;
import com.adongs.session.terminal.TerminalFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 权限自动配置
 * @author yudong
 * @version 1.0
 */
public class AuthoritySecurityAuto {

    /**
     * 配置默认的数据源
     * @return 数据源
     */
    @Bean
    @ConditionalOnMissingBean(SecurityDataSource.class)
    public SecurityDataSource dataSource(){
        return new DefaultSecurityDataSource();
    }

    /**
     * 配置redis缓存
     * @param config 配置信息
     * @return 缓存
     */
    @Bean
    @ConditionalOnClass(name = {"org.springframework.data.redis.core.RedisTemplate"})
    @ConditionalOnMissingBean(Cache.class)
    public Cache redisCache(AuthenticationConfig config){
        return new RedisCache(config.getCacheTime());
    }

    /**
     * 配置内存缓存
     * @param config 配置信息
     * @return 缓存
     */
    @Bean
    @ConditionalOnMissingBean(Cache.class)
    public Cache memoryCache(AuthenticationConfig config){
        return new MemoryCache(config.getCacheTime());
    }

    /**
     * 配置缓存管理器
     * @param cache 缓存
     * @param securityDataSource 数据源
     * @return 缓存管理器
     */
    @Bean
    public CacheManager cacheManager(Cache cache,SecurityDataSource securityDataSource){
        return new CacheManager(cache,securityDataSource);
    }

    /**
     * 配置用户认证和权限管理
     * @param authorityAggregator 权限聚集器
     * @param cacheManager 缓存管理器
     * @return 认证器
     */
    @Bean
    public AuthorityAuthentication authorityAuthentication(AuthorityAggregator authorityAggregator,CacheManager cacheManager){
        return new AuthorityAuthentication(authorityAggregator,cacheManager);
    }

    /**
     * 配置终端管理
     * @param config 配置
     * @param cacheManager 缓存管理
     * @param securityDataSource 数据源
     * @return filter
     */
    @Bean
    public TerminalFactory terminalFactory(AuthenticationConfig config,CacheManager cacheManager,SecurityDataSource securityDataSource){
       return new TerminalFactory(config,cacheManager,securityDataSource);
    }

    /**
     * 配置注解扫描
     * @return 注解扫描
     */
    @Bean
    public AuthenticationAspect authenticationAspect(){
        return new AuthenticationAspect();
    }


    /**
     * 配置重复读取
     * @param dispatcherServlet dispatcherServlet
     * @param context 上下文
     * @param terminalFactory 终端工厂
     * @return filter
     */
    @Bean
    public FilterRegistrationBean terminalFilter(DispatcherServlet dispatcherServlet, ApplicationContext context,TerminalFactory terminalFactory){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        TerminalFilter bodyReaderFilter = new TerminalFilter(terminalFactory,dispatcherServlet,context);
        filterRegistrationBean.setFilter(bodyReaderFilter);
        filterRegistrationBean.setOrder(100);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * 配置用户验证
     * @param dispatcherServlet dispatcherServlet
     * @param context 上下文
     * @param authentication 权限处理
     * @return filter
     */
    @Bean
    public FilterRegistrationBean userAuthenticationFilter(DispatcherServlet dispatcherServlet, ApplicationContext context,AuthorityAuthentication authentication){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter(dispatcherServlet,context,authentication);
        filterRegistrationBean.setFilter(userAuthenticationFilter);
        filterRegistrationBean.setOrder(101);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * 配置权限验证
     * @param dispatcherServlet dispatcherServlet
     * @param context 上下文
     * @param authentication 权限处理
     * @return filter
     */
    @Bean
    public FilterRegistrationBean authorityAuthenticationFilter(DispatcherServlet dispatcherServlet, ApplicationContext context,AuthorityAuthentication authentication){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        AuthorityAuthenticationFilter authenticationFilter = new AuthorityAuthenticationFilter(dispatcherServlet,context,authentication);
        filterRegistrationBean.setFilter(authenticationFilter);
        filterRegistrationBean.setOrder(102);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
