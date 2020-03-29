package com.security.manager.implement.decrypt.coding;

import com.google.common.io.BaseEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * @author yudong
 * @version 1.0
 * @date 2019/11/14 8:16 下午
 * @modified By
 */
public class RSA implements Coding{

    private final static Log LOGGER = LogFactory.getLog(Symmetric.class);

    private PrivateKey privateKey;

    public PublicKey publicKey;

    private Charset charset;

    private final static String DEFAULT_ENCODING="UTF-8";

    private final static String DEFAULT_ALGORITHM = "RSA";


    public RSA(String privateKey, String publicKey, String charset) {
        LOGGER.info("init RSA");
        this.privateKey = getPrivateKey(privateKey);
        this.publicKey = getPublicKey(publicKey);
        this.charset = Charset.forName(charset);;
    }

    public RSA(String privateKey, String publicKey) {
        this(privateKey,publicKey,DEFAULT_ENCODING);
    }


    public RSA(File privateKey, File publicKey, String fileEncoding, String charset){
        this(getFile(privateKey,fileEncoding),getFile(publicKey,fileEncoding),charset);
    }


    public RSA(File privateKey, File publicKey, String fileEncoding){
        this(getFile(privateKey,fileEncoding),getFile(publicKey,fileEncoding),DEFAULT_ENCODING);
    }



    /**
     * 编码
     *
     * @param str
     */
    @Override
    public String encode(String str) {
        byte[] resultBytes = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            resultBytes = cipher.doFinal(str.getBytes(charset));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return BaseEncoding.base64().encode(resultBytes);
    }

    /**
     * 解码
     *
     * @param str
     */
    @Override
    public String decode(String str) {
        byte[] resultBytes = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            resultBytes = cipher.doFinal(BaseEncoding.base64().decode(str));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new String(resultBytes, charset);
    }


    public PrivateKey getPrivateKey(String privateKey){
        PrivateKey key = null;
        try {
            byte[] bytes = BaseEncoding.base64().decode(privateKey);
            PKCS8EncodedKeySpec  keySpec= new PKCS8EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_ALGORITHM);
            key = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return key;
    }

    public PublicKey getPublicKey(String publicKey){
        PublicKey key = null;
        try {
            byte[] bytes = BaseEncoding.base64().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_ALGORITHM);
            key = keyFactory.generatePublic(keySpec);
        }  catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return key;
    }


    public static Map<String,String> createRASKey(int keysize){
        Map<String,String> keys = new HashMap<>();
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(DEFAULT_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(keysize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey =  keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        keys.put("publicKey",BaseEncoding.base64().encode(publicKey.getEncoded()));
        keys.put("privateKey",BaseEncoding.base64().encode(privateKey.getEncoded()));
        return keys;
    }


    private static String getFile(File key,String encoding){
        Long keyLength= key.length();
        byte[] fileContent = new byte[keyLength.intValue()];
        try(FileInputStream in = new FileInputStream(key)){
            in.read(fileContent);
            return new String(fileContent, encoding);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
