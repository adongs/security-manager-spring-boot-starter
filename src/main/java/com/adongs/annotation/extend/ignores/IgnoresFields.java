package com.adongs.annotation.extend.ignores;

import java.lang.annotation.*;

/**
 * 忽略返回值
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoresFields {

    /**
     * 忽略返回值
     * @return 忽略返回值组合
     */
    IgnoresField [] value();

}
