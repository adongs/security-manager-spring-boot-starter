package com.security.manager.annotation.core;

import java.lang.annotation.*;

/**
 * 版本控制
 * @author adong
 * @version 1.0
 * @date 2019/12/2 上午11:39
 * @modified by
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Version {

    /**
     * 允许版本
     * @return
     */
    String [] value();
}
