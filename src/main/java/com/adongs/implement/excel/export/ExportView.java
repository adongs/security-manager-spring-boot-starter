package com.adongs.implement.excel.export;

import cn.afterturn.easypoi.excel.entity.ExportParams;

import java.util.Collection;

/**
 * @author yudong
 * @version 1.0
 */
public class ExportView<T> {

    private ExportParams params;
    private Collection<T> data;
    private Class<T> clazz;

    public ExportView(ExportParams params, Collection<T> data, Class<T> clazz) {
        this.params = params;
        this.data = data;
        this.clazz = clazz;
    }

    public static ExportView build(ExportParams params, Collection<?> data){
        Class clazz = data.isEmpty()?Object.class:data.iterator().next().getClass();
        ExportView exportView = new ExportView(params,data,clazz);
        return exportView;
    }

    public ExportParams getParams() {
        return params;
    }

    public Collection<T> getData() {
        return data;
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
