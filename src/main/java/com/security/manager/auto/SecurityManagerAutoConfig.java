package com.security.manager.auto;

import com.security.manager.data.DataSource;
import com.security.manager.data.DefaultDataSource;
import com.security.manager.implement.core.CertificationAspect;
import com.security.manager.implement.core.RateLimiterAspect;
import com.security.manager.implement.core.SignAspect;
import com.security.manager.implement.core.VersionAspect;
import com.security.manager.implement.rateLimiter.DefaultRateLimiterProcessor;
import com.security.manager.implement.resubmit.DefaultResubmitProcessor;
import com.security.manager.implement.sign.DefaultSignProcessor;
import com.security.manager.session.manager.BodyReaderFilter;
import com.security.manager.session.manager.SessionFilter;
import com.security.manager.session.user.TerminalFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan("com.security.manager")
@Import({EncryptionAutoConfig.class, LogOutAutoConfig.class})
public class SecurityManagerAutoConfig {

    private final static Log LOGGER = LogFactory.getLog(SecurityManagerAutoConfig.class);

    /**
     * 开启权限
     * @param terminalFactory
     * @return
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
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.enabled",havingValue = "true",matchIfMissing = true)
    public CertificationAspect certificationAspect(){
        return new CertificationAspect();
    }

    /**
     * 默认的权限数据源
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    @ConditionalOnProperty(name = "spring.security.manager.request.enabled",havingValue = "true",matchIfMissing = true)
    public DefaultDataSource defaultDataSource(){
        return new DefaultDataSource();
    }

    /**
     * 构建终端工厂
     * @param securityManagerConfig
     * @param dataSource
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.enabled",havingValue = "true",matchIfMissing = true)
    public TerminalFactory terminalFactory(SecurityManagerConfig securityManagerConfig,DataSource dataSource){
        return new TerminalFactory(securityManagerConfig,dataSource);
    }

    /**
     * 配置重复读取
     * @return
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
     * @param securityManagerConfig
     * @return
     */
   @Bean
   @ConditionalOnExpression("#{environment.getProperty('spring.security.manager.request.permits-per-second')!=null}")
   public DefaultRateLimiterProcessor rateLimiterProcessor(SecurityManagerConfig securityManagerConfig){
       SecurityManagerConfig.Request request = securityManagerConfig.getRequest();
       return new DefaultRateLimiterProcessor(request.getPermitsPerSecond());
   }

    /**
     * 开启重复提交限制
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.resubmit.enabled",havingValue = "true",matchIfMissing = true)
    public RateLimiterAspect rateLimiterAspect(){
        return new RateLimiterAspect();
    }
    /**
     * 开启重复提交限制默认处理器
     * @return
     */
    @Bean
   @ConditionalOnProperty(name = "spring.security.manager.request.resubmit.enabled",havingValue = "true",matchIfMissing = true)
   public DefaultResubmitProcessor defaultResubmitProcessor(SecurityManagerConfig securityManagerConfig){
      SecurityManagerConfig.Resubmit resubmit = securityManagerConfig.getRequest().getResubmit();
      return new DefaultResubmitProcessor(resubmit.getInitCount(),resubmit.getDuration(), TimeUnit.MILLISECONDS);
   }

    /**
     * 开启盐值校验
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.readers",havingValue = "true",matchIfMissing = true)
   public SignAspect signAspect(){
        return new SignAspect();
   }

    /**
     * 开启盐值校验处理器
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.security.manager.request.readers",havingValue = "true",matchIfMissing = true)
   public DefaultSignProcessor defaultSignProcessor(){
        return new DefaultSignProcessor();
   }

    /**
     * 开启版本控制
     * @return
     */
   @Bean
   @ConditionalOnProperty(name = "spring.security.manager.request.enabled",havingValue = "true",matchIfMissing = true)
   public VersionAspect versionAspect(){
        return new VersionAspect();
   }

}
