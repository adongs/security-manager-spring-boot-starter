package com.adongs.exception;

/**
 * 图片验证码未通过
 * @author adong
 * @version 1.0
 */
public class CaptchaException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CaptchaException(String message) {
        super(message);
    }
}
