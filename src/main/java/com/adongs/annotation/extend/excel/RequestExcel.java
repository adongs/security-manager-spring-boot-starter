package com.adongs.annotation.extend.excel;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 将接收到的excel转换为数据
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestExcel {
    /**
     * 参数名称,如果为空将全部的文件类型导入
     * @return 参数名称
     */
    @AliasFor("name")
    String value() default "";
    /**
     * 参数名称,如果为空将全部的文件类型导入
     * @return 参数名称
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 读取sheet名称
     * @return sheet名称
     */
    String sheet() default "";
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
    /**
     * 文件保存路径,如果为空则不保存
     * @return 文件保存路径
     */
    String savePath() default "";

    /**
     * 自定义文件处理器
     * @return 自定义文件处理器
     */
    String processor() default "";
}
