package com.adongs.annotation.core;


import com.adongs.constant.RefreshTime;

import java.lang.annotation.*;

/**
 * 防止重复提交
 * @author yudong
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Resubmit {

    /**
     * 重复提交时间判定时间(单位毫秒)
     * 0 表示使用配置文件时间   -1不使用时间判断
     * @return 重复提交时间判定时间(单位毫秒)
     */
    long time() default 0;

    /**
     * 多次请求是否刷新时间 默认使用配置文件配置
     * @return 多次请求是否刷新时间
     */
    RefreshTime refresh() default RefreshTime.CONFIG;

    /**
     * 请求的唯一标识,支持el表达式
     * 如果不填写则自动生成
     * @return 请求的唯一标识,支持el表达式
     */
    String uid() default "";

    /**
     * 用户隔离,支持EL表达式,off表示不进行用户隔离,只接口进行判断
     * 如果使用当前的权限框架默认获取,如果使用三方权限框架,支持EL表达式获取
     * @return 用户隔离
     */
    String userId() default "";

    /**
     * 处理类 不填写在默认使用配置文件的处理
     * @return 处理类
     */
    String processor() default "";

}
