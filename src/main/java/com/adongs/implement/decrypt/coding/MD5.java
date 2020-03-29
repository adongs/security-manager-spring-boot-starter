package com.adongs.implement.decrypt.coding;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author yudong
 * @version 1.0
 */
public class MD5 extends Digest {

    private final static String DEFAULT_ALGORITHM = "MD5";
   private final static Charset DEFAULT_ENCODING= StandardCharsets.UTF_8;

    public MD5(String slat, Charset charset)throws NoSuchAlgorithmException,UnsupportedCharsetException {
        super(MessageDigest.getInstance(DEFAULT_ALGORITHM),slat ==null?null:slat.trim(),charset);
    }

    public MD5(String charset)throws NoSuchAlgorithmException,UnsupportedCharsetException {
        this(null,Charset.forName(charset));
    }

    public MD5()throws NoSuchAlgorithmException, UnsupportedCharsetException {
        this(null,DEFAULT_ENCODING);
    }


    @Override
    public String analysis(byte[] digest) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            stringBuilder.append(Integer.toHexString((0x000000FF & digest[i]) | 0xFFFFFF00).substring(6));
        }
        return stringBuilder.toString();
    }

   public static MD5 build(){
       try {
           MD5 md5 = new MD5();
           return md5;
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
           return null;
       }
   }

}
