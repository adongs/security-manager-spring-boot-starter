package com.adongs.implement.decrypt;

import com.adongs.implement.BaseProcessor;

public interface DecryptProcessor extends BaseProcessor {

    /**
     * 加密
     * @param type 加密方式
     * @param plaintext 明文
     * @return 密文
     */
    public String encryption(String type,String plaintext);

    /**
     * 解密
     * @param type 解密方式
     * @param ciphertext 密文
     * @return 明文
     */
    public String decode(String type,String ciphertext);
}
