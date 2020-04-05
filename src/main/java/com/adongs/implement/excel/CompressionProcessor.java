package com.adongs.implement.excel;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 压缩处理器
 * @author yudong
 * @version 1.0
 */
public class CompressionProcessor {
    /**缓冲字节*/
    public static final int BUFFER = 1024;


    public static void fileCompress(File file, HttpServletResponse httpServletResponse) throws IOException {
        zipCompress(file,httpServletResponse.getOutputStream());
        httpServletResponse.getOutputStream().flush();
        httpServletResponse.getOutputStream().close();
    }

    public static void fileCompress(ByteArrayOutputStream byteArrayOutputStream,String fileName, HttpServletResponse httpServletResponse) throws IOException {
        zipCompress(byteArrayOutputStream,fileName,httpServletResponse.getOutputStream());
        httpServletResponse.getOutputStream().flush();
        httpServletResponse.getOutputStream().close();
    }

    public static void zipCompress(ByteArrayOutputStream byteArrayOutputStream,String fileName,OutputStream os)throws IOException{
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try(ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(os)){
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(fileName);
            zipArchiveEntry.setSize(byteArrayOutputStream.size());
            zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
            IOUtils.copy(byteArrayInputStream,zipArchiveOutputStream);
            zipArchiveOutputStream.closeArchiveEntry();
        }catch (IOException e){
            throw e;
        }

    }


    public static void zipCompress(File file, OutputStream os) throws IOException {
        try(InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
           ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(os)){
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file.getName());
            zipArchiveEntry.setSize(file.length());
            zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
            IOUtils.copy(inputStream,zipArchiveOutputStream);
            zipArchiveOutputStream.closeArchiveEntry();
            file.delete();
        }catch (IOException e){
            throw e;
        }
    }

}
