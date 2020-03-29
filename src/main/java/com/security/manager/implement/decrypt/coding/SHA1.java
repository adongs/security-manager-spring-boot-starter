package com.security.manager.implement.decrypt.coding;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author yudong
 * @version 1.0
 * @date 2019/11/14 6:41 下午
 * @modified By
 */
public class SHA1 extends Digest {

    private final static String DEFAULT_ALGORITHM = "SHA";
    private final static String DEFAULT_ENCODING="UTF-8";

    public SHA1(String slat, String charset)throws NoSuchAlgorithmException,UnsupportedCharsetException {
        super(MessageDigest.getInstance(DEFAULT_ALGORITHM),slat ==null?null:slat.trim(),Charset.forName(charset));
    }

    public SHA1(String charset)throws NoSuchAlgorithmException,UnsupportedCharsetException {
        this(null,charset);
    }

    public SHA1()throws NoSuchAlgorithmException, UnsupportedCharsetException {
        this(DEFAULT_ENCODING);
    }

    @Override
    public String analysis(byte[] digest) {
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            int val = ((int) digest[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
