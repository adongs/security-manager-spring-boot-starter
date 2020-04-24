package com.adongs.implement.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelI18nHandler;
import com.adongs.annotation.extend.ResponseExcel;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.ApplicationContext;

import java.util.Collection;


/**
 * excel处理器
 * @author yudong
 * @version 1.0
 */
public class ExcelProcessor {

    private ApplicationContext applicationContext;

    public ExcelProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 小数据量导出
     * @param exportParams 导出参数
     * @param data 导出数据
     * @return excel对象
     */
    public Workbook export(ExportParams exportParams, Collection<?> data){
        Class<?> tClass = getTClass(data);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, tClass, data);
        data.clear();
        return workbook;
    }



    /**
     * 设置导出excel参数
     * @param title 标题名称
     * @param sheetName sheet名称
     * @param excelType 导出类型
     *                  HSSF:Excel97-2003版本，扩展名为.xls。一个sheet最大行数65536，最大列数256
     *                  XSSF:Excel2007版本开始，扩展名为.xlsx。一个sheet最大行数1048576，最大列数16384
     * @param iExcelI18nHandler 国际自定义实现
     * @param iExcelDataHandler 数据处理自定义实现
     * @param style 自定义风格
     * @return excel导出参数
     */
    public ExportParams exportParams(String title,
                                     String sheetName,
                                     ExcelType excelType,
                                     IExcelI18nHandler iExcelI18nHandler,
                                     IExcelDataHandler iExcelDataHandler,
                                     Class<? extends IExcelExportStyler> style){
        ExportParams exportParams = new ExportParams(title,sheetName,excelType);
        exportParams.setI18nHandler(iExcelI18nHandler);
        exportParams.setDataHandler(iExcelDataHandler);
        exportParams.setStyle(style);
     return exportParams;
    }

    /**
     * 设置导出excel参数
     * @param title 标题名称
     * @param sheetName sheet名称
     * @param responseExcel excel注解
     * @return excel导出参数
     */
    public ExportParams exportParams(String title,
                                     String sheetName,
                                     ResponseExcel responseExcel){
        IExcelI18nHandler i18nHandler = applicationContext.getBean(responseExcel.i18nHandler());
        IExcelDataHandler dataHandler = applicationContext.getBean(responseExcel.dataHandler());
       return exportParams(title,sheetName,responseExcel.excelType(),i18nHandler,dataHandler,responseExcel.style());
    }


    /**
     * 获取集合泛型
     * @param data
     * @return
     */
    private Class<?> getTClass(Collection<?> data){
        if(data.isEmpty()){
            return Object.class;
        }else{
            Object next = data.iterator().next();
            return next.getClass();

        }
    }
}
