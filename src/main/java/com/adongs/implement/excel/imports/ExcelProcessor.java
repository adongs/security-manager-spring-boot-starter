package com.adongs.implement.excel.imports;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adongs.exception.ExcelCheckException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

/**
 * @author yudong
 * @version 1.0
 */
public interface ExcelProcessor {

    /**
     * 保存文件
     * @param path 文件路径
     * @param collection 上传的文件
     */
  public void saveFile(String path, Collection<MultipartFile> collection);

    /**
     * 校验失败后
     * @param excelImportResult 导出失败数据
     */
  public void check(ExcelImportResult<Object> excelImportResult)throws ExcelCheckException;
}
