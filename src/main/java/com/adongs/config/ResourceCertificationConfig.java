package com.adongs.config;

import com.adongs.implement.core.resources.JwtAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 资源认证配置
 * @author yudong
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.resources")
public class ResourceCertificationConfig {

    /**
     * 资源鉴权令牌获取,在header中获取,如果header中获取不到将在param中获取
     */
    private String resources = "resources";

    /**
     * 秘钥
     */
    private String key;

    /**
     * 加密方式
     */
    private JwtAlgorithm mode = JwtAlgorithm.HMAC512;

    /**
     * 默认的处理器,当注解没有显示指定使用这个处理器
     */
    private String defaultProcessor = "default";

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JwtAlgorithm getMode() {
        return mode;
    }

    public void setMode(JwtAlgorithm mode) {
        this.mode = mode;
    }

    public String getDefaultProcessor() {
        return defaultProcessor;
    }

    public void setDefaultProcessor(String defaultProcessor) {
        this.defaultProcessor = defaultProcessor;
    }
}
