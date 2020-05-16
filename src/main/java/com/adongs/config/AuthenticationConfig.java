package com.adongs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * 权限管理配置
 * @author yudong
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.authenticate")
public class AuthenticationConfig {
    /**
     * 请求token的key,在header中获取
     */
    private String token = "token";
    /**
     * 缓存保存时间 默认是24小时
     */
    private long cacheTime =1000*60*60*24;
    /**
     * 忽略验证的url
     */
    private Set<String> ignoreUrl;
    /**
     * 权限文件,通过文件获取url的权限
     * 允许设置多个文件
     */
    private String [] authorityFile;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getIgnoreUrl() {
        return ignoreUrl;
    }

    public void setIgnoreUrl(Set<String> ignoreUrl) {
        this.ignoreUrl = ignoreUrl;
    }

    public String[] getAuthorityFile() {
        return authorityFile;
    }

    public void setAuthorityFile(String[] authorityFile) {
        this.authorityFile = authorityFile;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }
}
