package com.adongs.exception;


/**
 * 用户认证失败
 * @author adong
 * @version 1.0
 */
public class UserAuthenticationException extends RuntimeException {

    private String token;


    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param token 用户token
     */
    public UserAuthenticationException(String message, String token) {
        super(message);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
