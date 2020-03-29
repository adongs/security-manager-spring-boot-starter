package com.security.manager.implement.decrypt.coding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

/**
 * @author yudong
 * @version 1.0
 * @date 2019/11/15 10:07 上午
 * @modified By
 */
public class AES extends Symmetric {

    private final static Log LOGGER = LogFactory.getLog(AES.class);

    private final static String DEFAULT_ALGORITHM = "AES";

    private final static String DEFAULT_FILL_STYLE = "PKCS5Padding";

    private final static Charset DEFAULT_ENCODING= StandardCharsets.UTF_8;


    public AES(String key, String offset, Charset charset){
        super(DEFAULT_ALGORITHM,offset==null?"ECB":"CBC",DEFAULT_FILL_STYLE,key,offset, charset);
    }

    public AES(String key){
        this(key,null,DEFAULT_ENCODING);
    }

    @Override
    public SecretKey getSecretKey(String key) {
        Optional<String> optional = Optional.ofNullable(key);
        try {
            if(optional.isPresent()){
                KeyGenerator keyGenerator = KeyGenerator.getInstance(DEFAULT_ALGORITHM);
                keyGenerator.init(128, new SecureRandom(key.getBytes()));
                return keyGenerator.generateKey();
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public IvParameterSpec getIvParameterSpec(String offse) {
        Optional<String> optional = Optional.ofNullable(offse);
        if(optional.isPresent()){
            return new IvParameterSpec(optional.get().getBytes());
        }
        return null;
    }
}
