package com.adongs.annotation.extend.encryption;

import java.lang.annotation.*;

/**
 * 当方法参数是自定义类型的时候,用于指定某个类型需要加密或者解密处理
 * 例如:
 * 当中User类中的字段使用@Decode或者@Decrypt
 * public void test(@Cipher User user){}
 * 支持Collection 和 Map
 * @author yudong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
public @interface Cipher {

}
