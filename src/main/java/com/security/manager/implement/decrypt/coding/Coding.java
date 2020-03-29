package com.security.manager.implement.decrypt.coding;

/**
 * @author yudong
 * @version 1.0
 * @date 2019/11/14 6:14 下午
 * @modified By
 */
public interface Coding {

    /**
     * 编码
     */
    public String encode(String str);

    /**
     * 解码
     */
    public String decode(String str);
}
