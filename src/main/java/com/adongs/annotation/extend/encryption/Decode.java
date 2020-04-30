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
@Target(ElementType.METHOD)
@Documented
public @interface Decode {

    /**
     *
     *
     * 解密字段名称,如果是自定义的model需要指定字段,支持EL
     * @return 解密字段名称
     */
    String [] values() default "";

    /**
     * 自定义处理器
     * @return 自定义处理器
     */
    Class<? extends DecryptProcessor> processor() default DefaultDESDecryptProcessor.class;


}
