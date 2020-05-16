package com.adongs.annotation.extend.encryption;


import com.adongs.implement.decrypt.DecryptProcessor;
import com.adongs.implement.decrypt.DefaultDESDecryptProcessor;

import java.lang.annotation.*;

/**
 * 解密数据
 * 要求解析原生包装类型和List以及Map以及领域模型
 * @author adong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
public @interface Decode {

    /**
     * 解密方式
     * @return 解密字段名称
     */
    String value() default "";

    /**
     * 处理器名称
     * @return 处理器名称
     */
    String processor() default "";


}
