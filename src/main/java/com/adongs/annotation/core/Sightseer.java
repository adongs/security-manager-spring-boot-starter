package com.adongs.annotation.core;

import java.lang.annotation.*;

/**
 * 标识某些接口不需要登录即可访问
 * @author yudong
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Sightseer {
}
