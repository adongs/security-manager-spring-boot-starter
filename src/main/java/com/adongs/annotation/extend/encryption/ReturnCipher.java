package com.adongs.annotation.extend.encryption;

import com.adongs.constant.ReturnCipherType;

import java.lang.annotation.*;


/**
 * 标注返回值需要加密或者解密
 * 当返回值是String类型的时候,需要指定加密或者解密
 * 当返回值是自定义类型的时候,不需要指定加密或者解密,默认根据自定义类的中的Decode或者Decrypt决定加密或者解密
 * 支持Collection 和 Map
 * @author yudong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ReturnCipher {

    /**
     * 处理方式
     * @return 返回加密或者解密
     */
    ReturnCipherType value() default ReturnCipherType.MODEL;

    /**
     * 解密
     * @return 解密
     */
    Decode decode() default @Decode;

    /**
     * 加密
     * @return 加密
     */
    Decrypt decrypt() default @Decrypt;
}
