package com.adongs.annotation.core;

import com.adongs.constant.Logical;

import java.lang.annotation.*;

/**
 * 资源认证服务
 * jwt方式实现
 * @author yudong
 * @version 1.0
 */
@Target({ ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resources {

    /**
     * 权限组 支持模糊匹配
     * @return 权限数组
     */
    String [] permissions() default {};

    /**
     * 权限关系
     * @return 权限关系
     */
    Logical plogical() default Logical.AND;
}
