package com.adongs.annotation.extend;


import java.lang.annotation.*;

/**
 * 将数据导出为excel
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  ExcelExport {

    /**
     * 数据提取位置,支持el表达式
      * @return
     */
  String data();
    /**
     * 导出文件名称,支持el表达式
     * @return 导出文件名称
     */
  String name() default "导出数据.xls";

    /**
     * 导出sheet名称,支持el表达式
     * @return 导出sheet名称
     */
  String sheetName() default "sheet0";

}
