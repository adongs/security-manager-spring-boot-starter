package com.adongs.implement.excel.imports;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.adongs.annotation.extend.excel.RequestExcel;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * excel读取
 * @author yudong
 * @version 1.0
 */
@Component
public class ExportReadFactory {

    private final static Log LOGGER = LogFactory.getLog(ExportReadFactory.class);

    /**
     * 读取excel的sheet
     * @param file 文件
     * @return  读取excel的sheet
     */
    public Map<String,Integer> readSheet(MultipartFile file){
        Map<String,Integer> sheets = Maps.newHashMap();
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
           Workbook book = WorkbookFactory.create(byteArrayInputStream);) {
            for (int i = 0, n = book.getNumberOfSheets(); i < n; i++) {
                final Sheet sheetAt = book.getSheetAt(i);
                sheets.put(sheetAt.getSheetName(), i);
            }
        }catch (IOException e){
            LOGGER.error(e.getMessage(),e);
        }
        return sheets;
    }


    /**
     * 读取参数
     * @param excel excel注解
     * @param sheets sheet集合
     * @return 参数
     */
    public ImportParams importParams(RequestExcel excel,Map<String,Integer> sheets){
        ImportParams importParams = new ImportParams();
        final boolean check = excel.check();
        if (check){
            importParams.setNeedVerify(true);
        }
        if (excel.group().length>0){
            importParams.setVerifyGroup(excel.group());
        }
        final String sheet = excel.sheet();
        if (!StringUtils.isEmpty(sheet)){
            importParams.setStartSheetIndex(sheets.getOrDefault(sheet,0));
        }
        return importParams;
    }


    /**
     * 读取excel
     * @param file 文件
     * @param clazz 转换类型
     * @param importParams 读取
     * @param <T> 泛型
     * @return 数据
     */
   public <T> List<T> read(MultipartFile file, Class<T> clazz,ImportParams importParams){
       try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes())){
           List<T> list = ExcelImportUtil.importExcel(byteArrayInputStream, clazz, importParams);
           return list;
       } catch (Exception exception) {
           LOGGER.error(exception.getMessage(),exception);
           return Lists.newArrayList();
       }
   }


}
