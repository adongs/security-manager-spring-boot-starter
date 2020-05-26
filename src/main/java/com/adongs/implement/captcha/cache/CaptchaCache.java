package com.adongs.implement.captcha.cache;

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
     * @return ture 验证成功 false 验证失败
     */
    public boolean ver(String id,String verCode);

    /**
     * 添加标记
     * @param id id
     */
    public void addMarkerCount(String id);

    /**
     * 获取标记次数
     * @param id id
     * @return 获取标记次数
     */
    public int markerCount(String id);

    /**
     * 清除标记
     * @param id id
     */
    public void clearMark(String id);
}
