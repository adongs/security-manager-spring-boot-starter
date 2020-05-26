package com.adongs.implement.excel.export.handler;

import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelI18nHandler;

/**
 * @author yudong
 * @version 1.0
 */
public abstract class AbstractExcelHandler implements  ExcelHandler{


    @Override
    public Class<? extends IExcelExportStyler> style() {
        return ExcelExportStylerDefaultImpl.class;
    }

    @Override
    public IExcelDataHandler dataHandler() {
        return new ExcelDataHandlerDefaultImpl(){};
    }

    @Override
    public IExcelI18nHandler i18nHandler() {
        return new IExcelI18nHandler(){
            @Override
            public String getLocaleName(String name) {
                return name;
            }
        };
    }
}
