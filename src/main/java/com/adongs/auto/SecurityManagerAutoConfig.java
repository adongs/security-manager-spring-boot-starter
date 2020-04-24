package com.adongs.auto;

import com.adongs.implement.core.CertificationAspect;
import com.adongs.implement.lock.RedisLockAspect;
import com.adongs.implement.resubmit.DefaultResubmitProcessor;
import com.adongs.implement.sightseer.SightseerProcessor;
import com.adongs.implement.sightseer.SightseerRegistrar;
import com.adongs.implement.sign.DefaultSignProcessor;
import com.adongs.session.manager.BodyReaderFilter;
import com.adongs.session.user.TerminalFactory;
import com.adongs.data.DataSource;
import com.adongs.data.DefaultDataSource;
import com.adongs.implement.core.RateLimiterAspect;
import com.adongs.implement.core.SignAspect;
import com.adongs.implement.core.VersionAspect;
import com.adongs.implement.rateLimiter.DefaultRateLimiterProcessor;
import com.adongs.session.manager.SessionFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

/**
 * 自动配置主要类
 */
@Configuration
@ComponentScan("com.adongs")
@Import({EncryptionAutoConfig.class,
        ExcelAutoConfig.class,
        LogOutAutoConfig.class,
        ExcelWebMvcConfigurer.class,
        JedisLockAutoConfig.class,
        RedissonLockAutoConfig.class,
        ZookeeperLockAutoConfig.class})
@AutoConfigureAfter
public class SecurityManagerAutoConfig {

    private final static Log LOGGER = LogFactory.getLog(SecurityManagerAutoConfig.class);

    /**
     * 开启权限
     * @param terminalFactory 终端工厂
     * @return 权限过滤器
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.enabled",havingValue = "true",matchIfMissing = true)
    public FilterRegistrationBean sessionFilter(TerminalFactory terminalFactory){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        SessionFilter sessionFilter = new SessionFilter(terminalFactory);
        filterRegistrationBean.setFilter(sessionFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * 开启权限注解
     * @return 开启权限注解
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.enabled",havingValue = "true",matchIfMissing = true)
    public CertificationAspect certificationAspect(){
        return new CertificationAspect();
    }

    /**
     * 默认的权限数据源
     * @return 默认的权限数据源
     */
    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    @ConditionalOnProperty(name = "spring.security.manager.request.enabled",havingValue = "true",matchIfMissing = true)
    public DefaultDataSource defaultDataSource(){
        return new DefaultDataSource();
    }


    /**
     * 忽略url处理器
     * @param securityManagerConfig 配置
     * @param commonPrefix 公共前缀
     * @return 忽略处理器
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.tourists",havingValue = "false",matchIfMissing = true)
    public SightseerProcessor sightseerProcessor(SecurityManagerConfig securityManagerConfig, @Value("${server.servlet.context-path:}") String commonPrefix){
        return new SightseerProcessor(securityManagerConfig,commonPrefix);
    }

    /**
     * 游客注解扫描
     * @return 游客注解扫描器
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.tourists",havingValue = "false",matchIfMissing = true)
    public SightseerRegistrar sightseerRegistrar(){
        return new SightseerRegistrar();
    }

    /**
     * 构建终端工厂
     * @param securityManagerConfig 配置类
     * @param dataSource 数据源
     * @param sightseerProcessor 路径忽略器
     * @return 终端工厂
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.enabled",havingValue = "true",matchIfMissing = true)
    public TerminalFactory terminalFactory(SecurityManagerConfig securityManagerConfig,DataSource dataSource,SightseerProcessor sightseerProcessor){
        return new TerminalFactory(securityManagerConfig,dataSource,sightseerProcessor);
    }

    /**
     * 配置重复读取
     * @return  配置重复读取Filter
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.readers",havingValue = "true",matchIfMissing = true)
    public FilterRegistrationBean readerfilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        BodyReaderFilter bodyReaderFilter = new BodyReaderFilter();
        filterRegistrationBean.setFilter(bodyReaderFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * 开启限流
     * @param securityManagerConfig 配置类
     * @return 开启限流
     */
   @Bean
   @ConditionalOnExpression("#{environment.getProperty('spring.security.manager.request.permits-per-second')!=null}")
   public DefaultRateLimiterProcessor rateLimiterProcessor(SecurityManagerConfig securityManagerConfig){
       SecurityManagerConfig.Request request = securityManagerConfig.getRequest();
       return new DefaultRateLimiterProcessor(request.getPermitsPerSecond());
   }

    /**
     * 开启重复提交限制
     * @return 开启重复提交限制
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.resubmit.enabled",havingValue = "true",matchIfMissing = true)
    public RateLimiterAspect rateLimiterAspect(){
        return new RateLimiterAspect();
    }
    /**
     * 开启重复提交限制默认处理器
     * @param securityManagerConfig 配置类
     * @return 开启重复提交限制默认处理器
     */
    @Bean
   @ConditionalOnProperty(name = "spring.security.manager.request.resubmit.enabled",havingValue = "true",matchIfMissing = true)
   public DefaultResubmitProcessor defaultResubmitProcessor(SecurityManagerConfig securityManagerConfig){
      SecurityManagerConfig.Resubmit resubmit = securityManagerConfig.getRequest().getResubmit();
      return new DefaultResubmitProcessor(resubmit.getInitCount(),resubmit.getDuration(), TimeUnit.MILLISECONDS);
   }

    /**
     * 开启盐值校验
     * @return 开启盐值校验
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.readers",havingValue = "true",matchIfMissing = true)
   public SignAspect signAspect(){
        return new SignAspect();
   }

    /**
     * 开启盐值校验处理器
     * @return 开启盐值校验处理器
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.readers",havingValue = "true",matchIfMissing = true)
   public DefaultSignProcessor defaultSignProcessor(){
        return new DefaultSignProcessor();
   }

    /**
     * 开启版本控制
     * @return 开启版本控制
     */
   @Bean
   @ConditionalOnProperty(name = "spring.security.manager.request.enabled",havingValue = "true",matchIfMissing = true)
   public VersionAspect versionAspect(){
        return new VersionAspect();
   }


    /**
     * 开启互斥锁
     * @return 开启互斥锁
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.redis.enabled",havingValue = "true",matchIfMissing = true)
    public RedisLockAspect redisLockAspect(){
        return new RedisLockAspect();
    }
}
