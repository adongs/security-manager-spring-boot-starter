package com.adongs.implement.excel.imports.defaults;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adongs.exception.ExcelCheckException;
import com.adongs.implement.excel.imports.ExcelProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * 上传文件保存
 * @author yudong
 * @version 1.0
 */
@Component
public class DefaultFileProcessor implements ExcelProcessor {

    @Override
    public String name() {
        return "default";
    }

    /**
     * 保存文件,本方法为同步方法
     * 如果文件存在指定目录,将进行覆盖
     * 请尽量采用异步方式实现
     * @param path 文件路径
     * @param multipartFile 上传的文件
     */
    @Override
    public void saveFile(String path, MultipartFile multipartFile){
         if (!StringUtils.isEmpty(path)){
             path=path.lastIndexOf("/")==path.length()-1?path:path+"/";
             File file = new File(path+multipartFile.getOriginalFilename());
             if (!file.getParentFile().exists()) {
                 file.getParentFile().mkdirs();
             }
             try {
                 multipartFile.transferTo(file);
             }catch (IOException e){
                 e.printStackTrace();
             }
         }
    }

    @Override
    public void check(ExcelImportResult<Object> excelImportResult) throws ExcelCheckException {
         throw new ExcelCheckException("excel check data failure",excelImportResult.getFailList());
    }
}
