package com.adongs.annotation.extend.encryption;


import java.lang.annotation.*;

/**
 * 加密数据
 * @author adong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
public @interface Decrypt {

    /**
     * 加密方式
     * @return 解密字段名称
     */
    String value() default "";

    /**
     * 处理器名称
     * @return 处理器名称
     */
    String processor() default "";

}
