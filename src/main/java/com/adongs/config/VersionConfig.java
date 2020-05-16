package com.adongs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 版本控制配置
 * @author yudong
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.version")
public class VersionConfig {

    /**
     * 版本获取位置键的名称 优先获取header中 然后再获取param中
     */
    private String version = "version";


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
