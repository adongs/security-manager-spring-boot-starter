package com.adongs.annotation.core;

import java.lang.annotation.*;

/**
 * 版本控制
 * @author adong
 * @version 1.0
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Version {

    /**
     * 允许版本
     * @return 允许版本
     */
    String [] value();
}
