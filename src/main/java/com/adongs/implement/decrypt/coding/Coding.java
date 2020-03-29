package com.adongs.implement.decrypt.coding;

/**
 * @author yudong
 * @version 1.0
 */
public interface Coding {

    /**
     * 编码
     * @param str 明文
     * @return 密文
     */
    public String encode(String str);

    /**
     * 解码
     * @param str 密文
     * @return 明文
     */
    public String decode(String str);
}
