package com.adongs.annotation.extend.excel;

import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelI18nHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author yudong
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {
    /**
     * 数据提取位置,支持el表达式
     * @return 数据位置
     */
    @AliasFor("data")
    String value() default "";
    /**
     * 数据提取位置,支持el表达式
     * @return 数据位置
     */
    @AliasFor("value")
    String data() default "";
    /**
     * 表格名称
     * @return 表格名称
     */
    String title() default "";

    /**
     * 导出sheet名称
     * @return 导出sheet名称
     */
    String sheetName() default "";


    /**
     * 处理数据和风格处理器
     * @return 处理数据和风格处理器
     */
    String handler() default "";


}
