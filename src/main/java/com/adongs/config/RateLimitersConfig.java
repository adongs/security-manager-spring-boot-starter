package com.adongs.config;

import com.adongs.constant.CurrentLimitingAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 限流配置
 * @author yudong
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.limiting")
public class RateLimitersConfig {


    /**
     * 默认处理器
     */
    private String processor = "default";
    /**
     * 每秒允许访问量
     * 默认每秒访问量为100
     */
    private Double permitsPerSecond = 100d;

    /**
     * 获取令牌等待时间
     */
    private long permits = 0;

    /**
     * 使用算法,默认使用桶令牌算法
     */
    private CurrentLimitingAlgorithm arithmetic =CurrentLimitingAlgorithm.BURSTY;

    /**
     * 预热时间单位毫秒
     * 默认预热时间1分钟
     */
    private long warmupperiod = 1000*60;


    public Double getPermitsPerSecond() {
        return permitsPerSecond;
    }

    public void setPermitsPerSecond(Double permitsPerSecond) {
        this.permitsPerSecond = permitsPerSecond;
    }

    public CurrentLimitingAlgorithm getArithmetic() {
        return arithmetic;
    }

    public void setArithmetic(CurrentLimitingAlgorithm arithmetic) {
        this.arithmetic = arithmetic;
    }

    public long getWarmupperiod() {
        return warmupperiod;
    }

    public void setWarmupperiod(long warmupperiod) {
        this.warmupperiod = warmupperiod;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public long getPermits() {
        return permits;
    }

    public void setPermits(long permits) {
        this.permits = permits;
    }
}
