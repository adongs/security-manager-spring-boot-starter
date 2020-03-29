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
     * 导出文件名称
     * @return 导出文件名称
     */
  String name() default "导出数据.xls";

    /**
     * 导出sheet名称
     * @return 导出sheet名称
     */
  String sheetName() default "sheet0";

}
