package com.security.manager.implement.decrypt;

import com.security.manager.implement.decrypt.coding.Coding;
import com.security.manager.implement.decrypt.coding.DES;

import java.nio.charset.Charset;


public class DefaultDESDecryptProcessor implements  DecryptProcessor{

    private final Coding coding;

    public DefaultDESDecryptProcessor(String key) {
        coding = new DES(key);
    }
    public DefaultDESDecryptProcessor(String key,String offset) {
        coding = new DES(key,offset,Charset.forName("UTF-8"));
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @return 密文
     */
    @Override
    public String encryption(String plaintext) {
        return coding.encode(plaintext);
    }

    /**
     * 解密
     *
     * @param ciphertext 密文
     * @return 明文
     */
    @Override
    public String decode(String ciphertext) {
        return coding.decode(ciphertext);
    }
}
