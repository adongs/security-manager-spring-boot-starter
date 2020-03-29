package com.security.manager.annotation.core;

import com.security.manager.implement.resubmit.DefaultResubmitProcessor;
import com.security.manager.implement.resubmit.ResubmitProcessor;

import java.lang.annotation.*;

/**
 * 重复提交
 * @author yudong
 * @version 1.0
 * @date 2020/2/28 5:37 下午
 * @modified By
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Resubmit {

    /**
     * 重复提交时间判定时间(单位毫秒)
     * @return
     */
    long time() default 3000;

    /**
     * 多次请求是否刷新时间
     * @return
     */
    boolean refreshTime() default true;

    /**
     * 请求的唯一标识,支持el表达式
     * @return
     */
    String uid() default "";

    /**
     * 用户隔离,支持EL表达式
     * false 关闭
     * @return
     */
    String userId() default "";

    /**
     * 处理类
     * @return
     */
    Class<? extends ResubmitProcessor> processor() default DefaultResubmitProcessor.class;

}
