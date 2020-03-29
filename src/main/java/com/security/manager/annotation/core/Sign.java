package com.security.manager.annotation.core;


import com.security.manager.implement.sign.DefaultSignProcessor;
import com.security.manager.implement.sign.SignProcessor;

import java.lang.annotation.*;

/**
 * 盐值校验
 * 使用DefaultSignProcessor要求开启reques重复读取
 * @author adong
 * @version 1.0
 * @date 2019/12/2 上午11:39
 * @modified by
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sign {

    /**
     * 盐值支持el表达式
     * @return
     */
    String sign() default "";


    /**
     * 处理类
     * @return
     */
    Class<? extends SignProcessor> value() default DefaultSignProcessor.class;
}
