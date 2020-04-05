package com.adongs.implement.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.export.ExcelBatchExportService;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;


/**
 * 导出文件处理器
 * @author yudong
 * @version 1.0
 */
public class ExcelFileProcessor {

    /**
     * 每页数量
     */
    private final  static int pageSzie = 10000;

    /**
     * 导出文件内存形式
     * @param data 数据
     * @param fileName 文件名称
     * @param sheetName sheet名称
     * @return 内存对象
     */
   public static ByteArrayOutputStream exportMemory(Collection<?> data,String fileName,String sheetName) throws IOException {
       Class<?> clazz = getTClass(data);
       ExportParams exportParams = new ExportParams(null,sheetName);
       Workbook workbook = autoExport(data, exportParams, clazz);
       ByteArrayOutputStream byteArrayOutputStream = new  ByteArrayOutputStream();
       workbook.write(byteArrayOutputStream);
       byteArrayOutputStream.close();
       return byteArrayOutputStream;
   }

    /**
     * 导出文件使用缓存文件
     * @param data 数据
     * @param fileName 文件名称
     * @param sheetName sheet名称
     * @return 文件对象
     */
    public static File exportFile(Collection<?> data,String fileName,String sheetName) throws IOException {
        Class<?> clazz = getTClass(data);
        ExportParams exportParams = new ExportParams(null,sheetName);
        Workbook workbook = autoExport(data, exportParams, clazz);
        File file = File.createTempFile(fileName,".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        return file;
    }


    /**
     * 直接导出
     * @param data 数据
     * @param sheetName sheet名称
     * @param response 响应体
     * @throws IOException io异常
     */
   public static void export(Collection<?> data, String sheetName, HttpServletResponse response) throws IOException {
       Class<?> clazz = getTClass(data);
       ExportParams exportParams = new ExportParams(null,sheetName);
       Workbook workbook = autoExport(data, exportParams, clazz);
       workbook.write(response.getOutputStream());
       response.getOutputStream().flush();
       response.getOutputStream().close();
   }

    /**
     * 自动优化导出 当导出数量大于1万条时自动优化导出
     * @param data
     * @param clazz
     */
   private static Workbook autoExport(Collection<?> data,ExportParams exportParams,Class<?> clazz){
       if (data.size()>pageSzie){
           CollectionPagination collectionPagination = new CollectionPagination(data,pageSzie);
           ExcelBatchExportService batchServer = new ExcelBatchExportService();
           batchServer.init(exportParams, clazz);
           while (collectionPagination.hasNext()){
               batchServer.appendData(collectionPagination.next());
           }
          return batchServer.closeExportBigExcel();
       }else{
          return ExcelExportUtil.exportExcel(exportParams,clazz, data);
       }
   }



    /**
     * 获取集合泛型
     * @param data
     * @return
     */
   private static Class<?> getTClass(Collection<?> data){
       if(data.isEmpty()){
           return Object.class;
       }else{
           Object next = data.iterator().next();
           return next.getClass();

       }
   }


}
