package com.security.manager.annotation.extend;

import java.lang.annotation.*;

/**
 * 忽略返回值
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoresFields {

    IgnoresField [] value();

}
