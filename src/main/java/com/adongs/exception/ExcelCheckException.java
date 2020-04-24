package com.adongs.exception;

/**
 * excel验证不通过
 * @author yudong
 * @version 1.0
 */
public class ExcelCheckException extends RuntimeException{

    /**
     * 失败的数据
     */
    private Object failureData;

    public ExcelCheckException(String message, Object failureData) {
        super(message);
        this.failureData = failureData;
    }

    public Object getFailureData() {
        return failureData;
    }
}
