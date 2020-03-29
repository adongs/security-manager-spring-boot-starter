package com.adongs.implement.decrypt.coding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 摘要算法
 * @author adong
 * @version 1.0
 */
public abstract class Digest implements Coding{

    private final static Log LOGGER = LogFactory.getLog(Symmetric.class);

    private MessageDigest messageDigest;

    private  String  slat;

    private Charset charset;

    public Digest(MessageDigest messageDigest, String slat, Charset charset) {
        LOGGER.info("init messageDigest="+messageDigest.getAlgorithm());
        this.messageDigest = messageDigest;
        this.slat = slat;
        this.charset = charset;
    }

    /**
     * 混合slat
     * @param slat 混合字符串
     * @param str 加密字符串
     * @return 返回混合后的字符串
     */
    public  String blendSlat(String str,String slat){
        if (slat != null && slat.length()!=0){
            str = slat+str+slat;
        }
        return str;
    }


    /**
     * 解析
     * @param digest 解析字节数组
     * @return 解析后的字符串
     */
    public abstract String analysis(byte[] digest);

    @Override
    public String encode(String str) {
        if (str ==null || str.length()==0){
            throw new NullPointerException("encode str is null");
        }
        str = blendSlat(str,slat);
        this.messageDigest.update(str.getBytes(this.charset));
        byte[] digest = this.messageDigest.digest();
        return analysis(digest);
    }

    @Override
    public String decode(String str) {
         throw new NullPointerException("no decryption");
    }

}
