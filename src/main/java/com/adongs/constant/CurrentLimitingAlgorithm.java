package com.adongs.constant;

/**
 * @author yudong
 * @version 1.0
 */
public enum CurrentLimitingAlgorithm {
    /**
     * 桶令牌
     */
    BURSTY,
    /**
     * 漏桶
     */
    WARMUP;
}
