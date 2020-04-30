package com.adongs.implement.decrypt.coding;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import sun.security.ec.ECKeyFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author yudong
 * @version 1.0
 */
public class RSA implements Coding{

    private final static Log LOGGER = LogFactory.getLog(Symmetric.class);

    private PrivateKey privateKey;

    public PublicKey publicKey;

    private Charset charset;

    private final static String DEFAULT_ENCODING="UTF-8";

    private final static String DEFAULT_ALGORITHM = "RSA";


    public RSA(String privateKey, String publicKey, String charset) {
        LOGGER.debug("init RSA");
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
     * @param str 字符串
     * @return 编码后的字符串
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
     * @param str 字符串
     * @return 解码后的字符串
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

    /**
     * 转换公钥
     * @param path 路径
     * @return 公钥对象
     * @throws IOException io 异常
     * @throws CertificateException 文件格式不是 cer crt  pem 将抛出异常
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static PublicKey getUrlPublicKey(String path)throws IOException, CertificateException,NoSuchAlgorithmException, InvalidKeySpecException{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(read(path));
        KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 转换私钥
     * @param path 路径
     * @return 私钥对象
     * @throws IOException io异常
     * @throws CertificateException 文件格式不是 cer crt  pem 将抛出异常
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static PrivateKey getUrlPrivateKey(String path)throws IOException, CertificateException,NoSuchAlgorithmException, InvalidKeySpecException{
        PKCS8EncodedKeySpec  keySpec= new PKCS8EncodedKeySpec(read(path));
        KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 转换私钥
     * @param path 路径
     * @return 私钥对象
     * @throws IOException io异常
     * @throws CertificateException 文件格式不是 cer crt  pem 将抛出异常
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static ECPrivateKey getUrlECPrivateKey(String path)throws IOException, CertificateException,NoSuchAlgorithmException, InvalidKeySpecException{
        PKCS8EncodedKeySpec  keySpec= new PKCS8EncodedKeySpec(read(path));
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return  (ECPrivateKey)keyFactory.generatePrivate(keySpec);
    }

    /**
     * 转换公钥
     * @param path 路径
     * @return 公钥对象
     * @throws IOException io 异常
     * @throws CertificateException 文件格式不是 cer crt  pem 将抛出异常
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static ECPublicKey getUrlECPublicKey(String path)throws IOException, CertificateException,NoSuchAlgorithmException, InvalidKeySpecException{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(read(path));
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return  (ECPublicKey)keyFactory.generatePrivate(keySpec);
    }

    /**
     * 读取路径下的秘钥文件
     * @param path 路径
     * @return 文件字节数组
     * @throws IOException io异常
     * @throws CertificateException 文件格式不是 cer crt  pem 将抛出异常
     */
    public static byte [] read(String path) throws IOException, CertificateException {
        ImmutableMap<String,Boolean> types = ImmutableMap.of("cer",false,"crt",false,"pem",true);
        Resource classPathResource = new ClassPathResource(path);
        boolean exists = classPathResource.exists();
        if (!exists){
            throw new NullPointerException("PublicKey path is null "+path);
        }
        boolean isFile = classPathResource.isFile();
        if (!isFile){
            throw new NullPointerException("PublicKey path Not a file "+path);
        }
        String filename = classPathResource.getFilename();
        String type = filename.substring(filename.indexOf("."));
        byte [] data = null;
        try(InputStream inputStream = classPathResource.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            IOUtils.copy(inputStream,bos);
            data = bos.toByteArray();
        }
        Boolean isBase64 = types.get(type);
        if (isBase64 == null){
            throw new CertificateException("Format conversion error, can only recognize cer, crt, pem"+path);
        }
        if (isBase64){
            data = BaseEncoding.base64().decode(new String(data));
        }
        return data;
    }

}
