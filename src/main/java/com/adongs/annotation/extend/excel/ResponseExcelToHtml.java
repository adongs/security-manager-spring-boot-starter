package com.adongs.annotation.extend.excel;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * excel转html预览
 * @author yudong
 * @version 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseExcelToHtml {

    /**
     * 预览地址
     * @return 预览地址
     */
    @AliasFor("value")
    String path() default "";

    /**
     * 预览地址
     * @return 预览地址
     */
    @AliasFor("path")
    String value() default "";


}
