package com.adongs.annotation.core;

import com.adongs.implement.resubmit.DefaultResubmitProcessor;
import com.adongs.implement.resubmit.ResubmitProcessor;

import java.lang.annotation.*;

/**
 * 重复提交
 * @author yudong
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Resubmit {

    /**
     * 重复提交时间判定时间(单位毫秒)
     * @return 重复提交时间判定时间(单位毫秒)
     */
    long time() default 3000;

    /**
     * 多次请求是否刷新时间
     * @return 多次请求是否刷新时间
     */
    boolean refreshTime() default true;

    /**
     * 请求的唯一标识,支持el表达式
     * @return 请求的唯一标识,支持el表达式
     */
    String uid() default "";

    /**
     * 用户隔离,支持EL表达式
     * false 关闭
     * @return 用户隔离
     */
    String userId() default "";

    /**
     * 处理类
     * @return 处理类
     */
    Class<? extends ResubmitProcessor> processor() default DefaultResubmitProcessor.class;

}
