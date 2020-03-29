package com.adongs.implement.decrypt.coding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

/**
 * @author yudong
 * @version 1.0
 */
public class DES extends Symmetric {

    private final static Log LOGGER = LogFactory.getLog(DES.class);

    private final static String DEFAULT_ALGORITHM = "DES";

    private final static String DEFAULT_FILL_STYLE = "PKCS5Padding";

    private final static Charset DEFAULT_ENCODING= StandardCharsets.UTF_8;


    public DES(String key, String offset, Charset charset){
        super(DEFAULT_ALGORITHM,offset==null?"ECB":"CBC",DEFAULT_FILL_STYLE,key,offset,charset);
    }

    public DES(String key){
        this(key,null,DEFAULT_ENCODING);
    }

    @Override
    public SecretKey getSecretKey(String key) {
        Optional<String> optional = Optional.ofNullable(key);
        try {
            if(optional.isPresent()){
                DESKeySpec dks = new DESKeySpec(optional.get().getBytes());
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DEFAULT_ALGORITHM);
                return keyFactory.generateSecret(dks);
            }
        } catch (InvalidKeyException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (InvalidKeySpecException e) {
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
