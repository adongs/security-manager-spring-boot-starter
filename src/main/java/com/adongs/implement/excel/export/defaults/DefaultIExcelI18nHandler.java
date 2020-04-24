package com.adongs.implement.excel.export.defaults;

import cn.afterturn.easypoi.handler.inter.IExcelI18nHandler;
import org.springframework.stereotype.Component;

/**
 * @author yudong
 * @version 1.0
 */
@Component
public class DefaultIExcelI18nHandler implements IExcelI18nHandler {
    @Override
    public String getLocaleName(String name) {
        return name;
    }
}
