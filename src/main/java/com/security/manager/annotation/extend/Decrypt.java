package com.security.manager.annotation.extend;

import com.security.manager.implement.decrypt.DecryptProcessor;
import com.security.manager.implement.decrypt.DefaultDESDecryptProcessor;

import java.lang.annotation.*;

/**
 * 加密数据
 * @author adong
 * @version 1.0
 * @date 2019/11/21 下午2:43
 * @modified by
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Decrypt {

    /**
     * 加密字段名称,如果是自定义的model需要指定字段,支持EL
     * @return
     */
    String [] values() default "";

    /**
     * 自定义处理器
     * @return
     */
    Class<? extends DecryptProcessor> processor() default DefaultDESDecryptProcessor.class;


}
