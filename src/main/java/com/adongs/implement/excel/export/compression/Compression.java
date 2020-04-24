package com.adongs.implement.excel.export.compression;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 压缩自定义实现
 * @author yudong
 * @version 1.0
 */
public interface Compression {

    /**
     * 压缩
     * @param export excel对象
     * @param fileName 文件名称,带后缀(file是临时文件,导致文件名称被加上了随机数据,无法获取到原始的文件名称)
     * @param outputStream  压缩后输出到新的输出流中
     * @throws IOException io异常
     */
    public void compress(Workbook export, String fileName, OutputStream outputStream)throws IOException;

    /**
     * 压缩类型
     * @return 返回压缩类型
     */
    public String compressType();
}
