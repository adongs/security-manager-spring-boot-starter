package com.adongs.annotation.extend;

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
     * @return 过那个类的属性
     */
    Class<?> type();

    /**
     * 只保留字段
     * @return 只保留字段
     */
    String [] include() default {};

    /**
     * 过滤字段
     * @return 过滤字段
     */
    String [] filter() default {};

}
