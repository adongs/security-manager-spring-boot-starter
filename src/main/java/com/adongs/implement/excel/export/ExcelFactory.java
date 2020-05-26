package com.adongs.implement.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import com.adongs.annotation.extend.excel.Sheet;
import com.adongs.config.ExcelConfig;
import com.adongs.implement.excel.export.handler.ExcelHandler;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * excel处理器
 * @author yudong
 * @version 1.0
 */
@Component
public class ExcelFactory {

    private final static Map<String, ExcelHandler> HANDLER = Maps.newHashMap();
    public static int USE_SXSSF_LIMIT = 100000;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ExcelConfig config;


    /**
     * 导出多sheet
     * @param exportViews 导出集合
     * @param excelType 导出风格
     * @param release 是否自动释放数据集
     * @return 导出多sheet
     */
    public Workbook export(Collection<ExportView> exportViews,ExcelType excelType,boolean release){
        final Workbook workbook = getWorkbook(excelType, 0);
        ExcelExportService excelExportService = new ExcelExportService();
        for (ExportView exportView : exportViews) {
            excelExportService.createSheet(workbook,exportView.getParams(),exportView.getClazz(),exportView.getData());
            if (release){
                exportView.getData().clear();
            }
        }
        return workbook;
    }


    /**
     * 导出单个sheet
     * @param exportParams 导出参数
     * @param data 导出数据
     * @return excel对象
     */
    public Workbook export(ExportParams exportParams, Collection<?> data,ExcelType excelType,boolean release){
        Set<ExportView> exportViews  = Sets.newHashSet(ExportView.build(exportParams,data));
        return export(exportViews,excelType,release);
    }

    /**
     * 设置导出excel参数
     * @param sheet sheet
     * @param excelType 导出类型
     *                  HSSF:Excel97-2003版本，扩展名为.xls。一个sheet最大行数65536，最大列数256
     *                  XSSF:Excel2007版本开始，扩展名为.xlsx。一个sheet最大行数1048576，最大列数16384
     * @return excel导出参
     */
    public ExportParams exportParams(Sheet sheet,ExcelType excelType){
        final String name = name(sheet);
        final ExcelHandler handler = getHandler(name);
        ExportParams exportParams = new ExportParams(sheet.title(),sheet.sheetName(),excelType);
        exportParams.setStyle(handler.style());
        exportParams.setDataHandler(exportParams.getDataHandler());
        exportParams.setI18nHandler(exportParams.getI18nHandler());
        return exportParams;
    }

    /**
     * 获取Workbook
     * @param type 类型
     * @param size 数量
     * @return 获取Workbook
     */
    private  Workbook getWorkbook(ExcelType type, int size) {
        if (ExcelType.HSSF.equals(type)) {
            return new HSSFWorkbook();
        } else if (size < USE_SXSSF_LIMIT) {
            return new XSSFWorkbook();
        } else {
            return new SXSSFWorkbook();
        }
    }

    /**
     * 获取处理器
     * @param name 名称
     * @return 处理器
     */
    private ExcelHandler getHandler(String name){
        if (HANDLER.isEmpty()){
            final Map<String, ExcelHandler> beansOfType = applicationContext.getBeansOfType(ExcelHandler.class);
            if (beansOfType!=null){
                for (ExcelHandler value : beansOfType.values()) {
                    HANDLER.put(value.name(),value);
                }
            }
        }
        return HANDLER.get(name);
    }

    /**
     * 获取header名称
     * @param sheet
     * @return 获取header名称
     */
    private String name(Sheet sheet){
        if (StringUtils.isEmpty(sheet.handler())){
            return config.getHandler();
        }
        return sheet.handler();
    }

}
