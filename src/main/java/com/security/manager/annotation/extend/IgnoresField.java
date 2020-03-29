package com.security.manager.annotation.extend;

import java.lang.annotation.*;

/**
 * 忽略返回值
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(IgnoresFields.class)
public @interface IgnoresField {
    /**
     * 过那个类的属性
     * @return
     */
    Class<?> type();

    /**
     * 只保留字段
     * @return
     */
    String [] include() default {};

    /**
     * 过滤字段
     * @return
     */
    String [] filter() default {};

}
