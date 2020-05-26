package com.adongs.config;

import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * excel导出配置
 * @author yudong
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.excel")
public class ExcelConfig {

    /**
     * 自定义header 格式 key:value
     */
    private String [] headers = new String[] {};

    /**
     * 默认导出版本
     */
    private ExcelType excelType = ExcelType.HSSF;

    /**
     * 在名称上加上时间戳格式
     */
    private String format = "yyyy-MM-dd";
    /**
     * 时间戳位置 -1(名称前面)  0(关闭时间戳)  1(名称后面)
     */
    private int position = 1;

    /**
     * 默认处理风格
     */
    private String handler = "default";

    /**
     * 是否自动释放集合
     */
    private boolean release = true;

    /**
     * 导入默认处理器
     */
    private String importProcessor = "default";

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public ExcelType getExcelType() {
        return excelType;
    }

    public void setExcelType(ExcelType excelType) {
        this.excelType = excelType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public boolean getRelease() {
        return release;
    }

    public void setRelease(boolean release) {
        this.release = release;
    }

    public String getImportProcessor() {
        return importProcessor;
    }

    public void setImportProcessor(String importProcessor) {
        this.importProcessor = importProcessor;
    }
}

