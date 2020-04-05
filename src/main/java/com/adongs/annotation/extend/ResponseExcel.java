package com.adongs.annotation.extend;


import java.lang.annotation.*;

/**
 * 将数据导出为excel
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseExcel {

    /**
     * 数据提取位置,支持el表达式
      * @return
     */
  String data() default "";
    /**
     * 导出文件名称,支持el表达式
     * @return 导出文件名称
     */
  String name() default "导出数据";

  /**
   * 在名称上加上时间戳
   * @return
   */
  String nameDatetime() default "yyyy-MM-dd";

  /**
   * 时间戳的位置  -1(名称前面)  0(关闭时间戳)  1(名称后面)
   * @return
   */
  int nameDatetimePosition() default 1;

    /**
     * 导出sheet名称,支持el表达式
     * @return 导出sheet名称
     */
  String sheetName() default "sheet0";

  /**
   * 是否压缩
   * @return
   */
  boolean compression() default false;

  /**
   * 压缩格式
   * @return
   */
  CompressionFormat compressionFormat() default CompressionFormat.ZIP;




  public static enum CompressionFormat{
    ZIP

  }

}
