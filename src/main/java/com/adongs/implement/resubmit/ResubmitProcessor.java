package com.adongs.implement.resubmit;

import java.util.Map;

/**
 * @author yudong
 * @version 1.0
 */
public interface ResubmitProcessor {

    /**
     * 获取用户id
     * @param el el表达式
     * @param params 方法所有参数
     * @return 用户id
     */
    public String userId(String el,Map<String,Object> params);

    /**
     * 构建唯一标识
     * @param uid el表达式
     * @param params 方法所有参数
     * @return 构建唯一标识
     */
    public String key(String uid, Map<String,Object> params);

    /**
     * 保存唯一标识
     * @param key 唯一标识
     * @param time 保存时间
     */
    public void saveKey(String key,long time);

    /**
     * 判断是否存在
     * @param key 唯一标识
     * @return 判断是否存在true 存在  反之 false
     */
    public boolean isPresence(String key);


    /**
     * 更新时间
     * @param key 唯一标识
     * @param time 单位毫秒
     */
    public void update(String key,long time);

}
