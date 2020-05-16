package com.adongs.constant;

/**
 * 接口访问失败条件
 * @author yudong
 * @version 1.0
 */
public enum FailureCondition {
    /**
     * 当响应返回不是200
     */
    HTTP_STATUS_NOT_200,
    /**
     * 方法抛出异常
     */
    THROW_EXCEPTION,
    /**
     * 表达式判定
     */
    EXPRESSION_DECISION,
    /**
     * 自定义判定
     */
    CUSTOM_JUDGMENT,
    /**
     * 使用配置文件
     */
    CONFIG;
}
