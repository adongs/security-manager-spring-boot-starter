package com.adongs.implement.decrypt;

import com.adongs.implement.decrypt.coding.Coding;
import com.adongs.implement.decrypt.coding.DES;

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
     * @param type 加密
     * @param plaintext 明文
     * @return 密文
     */
    @Override
    public String encryption(String type, String plaintext) {
        return coding.encode(plaintext);
    }

    /**
     * 解密
     * @param type 解密类型
     * @param ciphertext 密文
     * @return 明文
     */
    @Override
    public String decode(String type, String ciphertext) {
        return coding.decode(ciphertext);
    }
    @Override
    public String name() {
        return "default";
    }
}
