package com.adongs.implement.excel.export.handler;

import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelI18nHandler;

/**
 * @author yudong
 * @version 1.0
 */
public interface ExcelHandler {


    /**
     * header名称
     * @return header名称
     */
    public String name();
    /**
     * 数据风格
     * @return 数据风格
     */
    public Class<? extends IExcelExportStyler> style();

    /**
     * 数据自定义处理
     * @return 数据自定义处理
     */
    public IExcelDataHandler dataHandler();

    /**
     * 国际化
     * @return 国际化
     */
    public IExcelI18nHandler i18nHandler();

}
