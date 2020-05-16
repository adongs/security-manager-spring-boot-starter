package com.adongs.annotation.extend.excel;


import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelI18nHandler;
import com.adongs.implement.excel.export.defaults.DefaultIExcelI18nHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 将数据导出为excel
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseExcel {

  /**
   * 导出文件名称,支持el表达式
   * @return 导出文件名称
   */
  @AliasFor("name")
  String value() default "导出数据";
  /**
   * 自定义响应header
   * @return 自定义响应header
   */
  String [] headers() default {};

    /**
     * 数据提取位置,支持el表达式
      * @return 数据位置
     */
  String data() default "";
    /**
     * 导出文件名称,支持el表达式
     * @return 导出文件名称
     */
  @AliasFor("value")
  String name() default "导出数据";

  /**
   * 导出多个sheet
   * @return
   */
  Sheet [] sheet() default {};

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
   * 导出版本
   * HSSF：Excel97-2003版本，扩展名为.xls。一个sheet最大行数65536，最大列数256
   * XSSF：Excel2007版本开始，扩展名为.xlsx。一个sheet最大行数1048576，最大列数16384
   * @return 导出版本
   */
  ExcelType excelType() default ExcelType.XSSF;

  /**
   * 在名称上加上时间戳
   * @return 名称时间模板
   */
  String nameDatetime() default "yyyy-MM-dd";

  /**
   * 时间戳的位置  -1(名称前面)  0(关闭时间戳)  1(名称后面)
   * @return 时间戳位置
   */
  int nameDatetimePosition() default 1;

  /**
   * 是否压缩
   * @return 是否压缩
   */
  boolean compression() default false;

  /**
   * 压缩格式 默认实现了zip
   * @return 压缩格式
   */
  String compressionFormat() default "zip";

}
