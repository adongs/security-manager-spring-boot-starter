package com.adongs.annotation.extend.excel;

import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelI18nHandler;
import com.adongs.implement.excel.export.defaults.DefaultIExcelI18nHandler;

import java.lang.annotation.*;

/**
 * @author yudong
 * @version 1.0
 * @date 2020/5/16 12:18 下午
 * @modified By
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {
    /**
     * 表格名称支持el表达式
     * @return 表格名称支持el表达式
     */
    String title() default "";

    /**
     * 导出sheet名称,支持el表达式
     * @return 导出sheet名称
     */
    String sheetName() default "sheet0";

    /**
     * 设置导出风格
     * @return 设置导出风格
     */
    Class<? extends IExcelExportStyler> style() default ExcelExportStylerDefaultImpl.class;

    /**
     * 数据转换器
     * @return 数据转换器
     */
    Class<? extends IExcelDataHandler> dataHandler() default ExcelDataHandlerDefaultImpl.class;

    /**
     * 国际化
     * @return 国际化
     */
    Class<? extends IExcelI18nHandler> i18nHandler() default DefaultIExcelI18nHandler.class;

    /**
     * 数据提取位置,支持el表达式
     * @return 数据位置
     */
    String data() default "";
}
