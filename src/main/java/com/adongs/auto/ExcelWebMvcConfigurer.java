package com.adongs.auto;

import com.adongs.implement.excel.ResponseExcelHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author yudong
 * @version 1.0
 */
public class ExcelWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new ResponseExcelHandler());
    }
}
