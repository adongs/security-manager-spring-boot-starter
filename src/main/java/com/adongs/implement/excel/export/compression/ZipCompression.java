package com.adongs.implement.excel.export.compression;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * zip压缩实现
 * @author yudong
 * @version 1.0
 */
public class ZipCompression implements Compression{


    /**
     * 压缩 当前方法会自动关闭流
     * @param export excel对象
     * @param fileName 文件名称,带后缀(file是临时文件,导致文件名称被加上了随机数据,无法获取到原始的文件名称)
     * @param newOutputStream  压缩后输出到新的输出流中
     * @throws IOException io异常
     */
    @Override
    public void compress(Workbook export,String fileName, OutputStream newOutputStream) throws IOException {
        try(ZipOutputStream zipArchiveOutputStream = new ZipOutputStream(newOutputStream)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipArchiveOutputStream.putNextEntry(zipEntry);
            export.write(zipArchiveOutputStream);
            zipArchiveOutputStream.closeEntry();
        }
    }

    @Override
    public String compressType() {
        return "zip";
    }
}
