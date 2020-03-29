package com.security.manager.annotation.core;


import java.lang.annotation.*;

/**
 * 权限认证
 * 要求必须是已经登录的用户
 * @author yudong
 * @version 1.0
 * @date 2019/10/23 3:36 下午
 * @modified By
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Certification {

    /**
     * 权限组
     * @return
     */
  String [] permissions() default {};

    /**
     * 权限关系
     * @return
     */
  Logical plogical() default Logical.AND;

    /**
     * 角色组
     * @return
     */
  String [] roles() default {};

    /**
     * 角色关系
     * @return
     */
  Logical rlogical() default Logical.AND;


  public static enum Logical {
    AND,OR
  }


}
