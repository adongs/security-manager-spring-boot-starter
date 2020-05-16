package com.adongs.implement.captcha;

/**
 * 请求验证码信息
 * @author yudong
 * @version 1.0
 */
public class RequestCode {
    /**
     * 验证码名称
     */
    private String code;
    /**
     * 登录用户名称
     */
    private String loginName;
    /**
     * 请求url
     */
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
