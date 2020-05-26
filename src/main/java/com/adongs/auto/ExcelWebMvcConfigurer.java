package com.adongs.auto;

import com.adongs.config.ExcelConfig;
import com.adongs.implement.excel.export.ExcelFactory;
import com.adongs.implement.excel.export.compression.CompressionManager;
import com.adongs.implement.excel.imports.ExportReadFactory;
import com.adongs.implement.excel.imports.RequestExcelArgumentResolver;
import com.adongs.implement.excel.export.ResponseExcelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author yudong
 * @version 1.0
 */
public class ExcelWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CompressionManager compressionManager;

    @Autowired
    private ExcelConfig config;

    @Autowired
    private ExcelFactory excelFactory;

    @Autowired
    private ExportReadFactory exportReadFactory;

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new ResponseExcelHandler(applicationContext,config,excelFactory,compressionManager));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestExcelArgumentResolver(applicationContext,exportReadFactory,config));
    }
}
