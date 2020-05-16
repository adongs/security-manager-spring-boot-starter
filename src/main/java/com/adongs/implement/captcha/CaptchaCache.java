package com.adongs.implement.captcha;

/**
 * 验证码缓存
 * @author yudong
 * @version 1.0
 */
public interface CaptchaCache {

    /**
     * 保存验证码
     * @param id 唯一id
     * @param verCode 验证码
     * @param time 有效时间
     */
    public void save(String id,String verCode,long time);

    /**
     * 验证二维码
     * @param id 唯一id
     * @param verCode 验证码
     * @return
     */
    public boolean ver(String id,String verCode);
}
