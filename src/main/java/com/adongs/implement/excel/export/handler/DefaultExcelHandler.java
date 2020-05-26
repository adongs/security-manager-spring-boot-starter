package com.adongs.implement.excel.export.handler;

import org.springframework.stereotype.Component;

/**
 * 默认实现处理器
 * @author yudong
 * @version 1.0
 */
@Component
public class DefaultExcelHandler extends AbstractExcelHandler{
    @Override
    public String name() {
        return "default";
    }
}
