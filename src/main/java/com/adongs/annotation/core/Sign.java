package com.adongs.annotation.core;


import com.adongs.implement.sign.DefaultSignProcessor;
import com.adongs.implement.sign.SignProcessor;

import java.lang.annotation.*;

/**
 * 盐值校验
 * 使用DefaultSignProcessor要求开启reques重复读取
 * @author adong
 * @version 1.0
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sign {

    /**
     * 盐值支持el表达式
     * @return 盐值支持el表达式
     */
    String sign() default "";


    /**
     * 处理类
     * @return 处理类
     */
    Class<? extends SignProcessor> value() default DefaultSignProcessor.class;
}
