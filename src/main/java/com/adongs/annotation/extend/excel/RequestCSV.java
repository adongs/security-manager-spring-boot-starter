package com.adongs.annotation.extend.excel;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 导入csv
 * @author yudong
 * @version 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestCSV {
    /**
     * 参数名称
     * @return 参数名称
     */
    @AliasFor("name")
    String value();
    /**
     * 参数名称
     * @return 参数名称
     */
    @AliasFor("value")
    String name();

    /**
     * 分割符 默认是使用配置文件
     * @return 分割符
     */
    String splitter() default "";

    /**
     * 是否开启校验
     * @return 是否开启校验
     */
    boolean check() default false;
    /**
     * 校验组
     * @return 校验组
     */
    Class<?> [] group() default {};

}
