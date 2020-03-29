package com.adongs.implement.decrypt.coding;


import com.google.common.io.BaseEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 对称加密
 * @author yudong
 * @version 1.0
 */
public abstract class Symmetric implements Coding{

    private final static Log LOGGER = LogFactory.getLog(Symmetric.class);

    private final String DEFAULT_CIPHER_ALGORITHM;

    private SecretKey secretKey;

    private IvParameterSpec iv;

    private Charset charset;

    public Symmetric(String algorithm, String encryption, String fillStyle, String key, String offse, Charset charset){
        LOGGER.info("init symmetric coding DEFAULT_CIPHER_ALGORITHM ="+algorithm+"/"+encryption+"/"+fillStyle);
        DEFAULT_CIPHER_ALGORITHM=algorithm+"/"+encryption+"/"+fillStyle;
        this.secretKey = getSecretKey(key);
        if (this.secretKey==null){
            throw new NullPointerException("secretKey is null");
        }
        this.iv = getIvParameterSpec(offse);
        this.charset =charset;
    }


    public abstract SecretKey getSecretKey(String key);

    public abstract IvParameterSpec getIvParameterSpec(String offse);

    /**
     * 编码
     * @param str 字符串
     * @return 解码后的字符串
     */
    @Override
    public String encode(String str) {
        byte[] bytes;
        try {
            Cipher enCipher = getCipher(Cipher.ENCRYPT_MODE);
            bytes = enCipher.doFinal(str.getBytes(charset));
            return BaseEncoding.base64().encode(bytes);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (InvalidKeyException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (InvalidAlgorithmParameterException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (BadPaddingException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 解码
     * @param str 字符串
     * @return 编码后的字符串
     */
    @Override
    public String decode(String str) {
        byte[] encrypted;
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            byte[] buffer = BaseEncoding.base64().decode(str);
            encrypted = cipher.doFinal(buffer);
            return new String(encrypted, charset);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (InvalidKeyException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (InvalidAlgorithmParameterException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (BadPaddingException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 获取算法工具
     * @param opmode opmode
     * @return Cipher
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws InvalidKeyException InvalidKeyException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    private Cipher getCipher(int opmode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        if(iv!=null) {
            cipher.init(opmode, secretKey, iv);
        }else{
            cipher.init(opmode, secretKey);
        }
        return cipher;
    }
}
