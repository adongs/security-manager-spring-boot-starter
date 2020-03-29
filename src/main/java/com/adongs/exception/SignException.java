package com.adongs.exception;

/**
 * token认证未通过
 * @author adong
 * @version 1.0
 */
public class SignException extends RuntimeException {

    private String sign;

    private String calculationSign;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SignException(String message) {
        super(message);
    }
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param calculationSign  计算后的盐值
     * @param sign 盐值
     */
    public SignException(String message, String sign, String calculationSign) {
        super(message);
        this.sign = sign;
        this.calculationSign = calculationSign;
    }

    public String getSign() {
        return sign;
    }

    public String getCalculationSign() {
        return calculationSign;
    }
}
