package com.adongs.annotation.extend.excel;


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
   * 访问路径
   * @return  访问路径
   */
  String path() default "";
  /**
   * 导出文件名称,支持el表达式
   * @return 导出文件名称
   */
  @AliasFor("name")
  String value() default "导出数据";

  /**
   * 导出文件名称,支持el表达式
   * @return 导出文件名称
   */
  @AliasFor("value")
  String name() default "导出数据";


  /**
   * 自定义响应header
   * @return 自定义响应header
   */
  String [] headers() default {};

  /**
   * 导出多个sheet
   * @return
   */
  Sheet [] sheet() default {@Sheet};

  /**
   * 导出版本
   * HSSF：Excel97-2003版本，扩展名为.xls。一个sheet最大行数65536，最大列数256
   * XSSF：Excel2007版本开始，扩展名为.xlsx。一个sheet最大行数1048576，最大列数16384
   * @return 导出版本
   */
  String excelType() default "";

  /**
   * 在名称上加上时间戳格式,默认使用配置
   * @return 名称时间模板
   */
  String format () default "";

  /**
   * 时间戳的位置  -2(使用配置文件) -1(名称前面)  0(关闭时间戳)  1(名称后面)
   * @return 时间戳位置
   */
  int position() default -2;

  /**
   * 压缩格式 默认实现了zip,如果不指定就不压缩
   * @return 压缩格式
   */
  String compression() default "";

}
