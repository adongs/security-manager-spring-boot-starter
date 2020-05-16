package com.adongs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 重复提交配置
 * @author yudong
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.resubmit")
public class ResubmitConfig {

    /**
     * 重复提交判定时间单位毫秒
     * 默认为5分钟
     */
    private long time = 50000;

    /**
     * 多次请求是否刷新时间 默认刷新
     */
    private boolean refresh = true;

    /**
     * 默认处理器
     */
    private String processor = "default";

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }
}
