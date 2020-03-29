package com.adongs.implement.decrypt;

public interface DecryptProcessor {

    /**
     * 加密
     * @param plaintext 明文
     * @return 密文
     */
    public String encryption(String plaintext);

    /**
     * 解密
     * @param ciphertext 密文
     * @return 明文
     */
    public String decode(String ciphertext);
}
