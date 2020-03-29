package com.security.manager.implement.resubmit;

import java.util.Map;

/**
 * @author yudong
 * @version 1.0
 * @date 2020/2/28 6:03 下午
 * @modified By
 */
public interface ResubmitProcessor {

    /**
     * 获取用户id
     * @param el
     * @return
     */
    public String userId(String el,Map<String,Object> params);

    /**
     * 构建唯一标识
     * @param uid
     * @param params
     * @return
     */
    public String key(String uid, Map<String,Object> params);

    /**
     * 保存唯一标识
     * @param key
     */
    public void saveKey(String key,long time);

    /**
     * 判断是否存在
     * @param key
     * @return
     */
    public boolean isPresence(String key);


    /**
     * 更新时间
     * @param time 单位毫秒
     */
    public void update(String key,long time);

}
