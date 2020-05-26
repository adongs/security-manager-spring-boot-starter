package com.adongs.implement.captcha.model;

/**
 * @author yudong
 * @version 1.0
 */
public class CaptchaModel {
    private String verCode;
    private long time;

    public CaptchaModel(String verCode, long time) {
        this.verCode = verCode;
        this.time = time;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
