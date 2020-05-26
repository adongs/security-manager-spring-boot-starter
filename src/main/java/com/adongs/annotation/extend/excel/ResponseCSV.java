package com.adongs.annotation.extend.excel;

/**
 * 导出csv
 * @author yudong
 * @version 1.0
 */
public @interface ResponseCSV {

    /**
     * 文件编码
     * @return
     */
    String encoding() default "";

    /**
     * 分割符 默认是使用配置文件
     * @return
     */
    String splitter() default "";
}
