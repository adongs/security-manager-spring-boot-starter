package com.adongs.implement.sign;

import com.adongs.implement.BaseProcessor;

import java.util.Map;

public interface SignProcessor extends BaseProcessor {

    /**
     * 获取sign
     * @param sign el表达式
     * @param param 请求参数
     * @return sign
     */
    public String sign(String sign, Map<String,Object> param);

    /**
     * 计算sign
     * @param param 请求参数
     * @return 计算sign
     */
    public String calculate(Map<String,Object> param);

}
