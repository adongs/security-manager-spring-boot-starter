package com.adongs.annotation.core;


import com.adongs.constant.Logical;

import java.lang.annotation.*;

/**
 * 权限认证
 * 要求必须是已经登录的用户
 * @author yudong
 * @version 1.0
 */
@Target({ ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authentication {

    /**
     * 权限组
     * @return 权限数组
     */
  String [] permissions() default {};

    /**
     * 权限关系
     * @return 权限关系
     */
  Logical plogical() default Logical.AND;

    /**
     * 角色组
     * @return 角色组
     */
  String [] roles() default {};

    /**
     * 角色关系
     * @return 角色关系
     */
  Logical rlogical() default Logical.AND;




}
