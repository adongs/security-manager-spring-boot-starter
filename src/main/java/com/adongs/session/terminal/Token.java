package com.adongs.session.terminal;

/**
 * token对象,负责解析token
 * @author yudong
 * @version 1.0
 */
public class Token {

    private final String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
