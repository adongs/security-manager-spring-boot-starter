package com.adongs.implement.core.resources;

import com.adongs.auto.SecurityManagerConfig;
import com.adongs.implement.decrypt.coding.RSA;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @author yudong
 * @version 1.0
 */
public enum JwtAlgorithm {

    RSA256(){
        @Override
        public Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig) {
            try {
                RSAPublicKey publicKey = (RSAPublicKey)RSA.getUrlPublicKey(jwtConfig.getPublicKey());
                RSAPrivateKey privateKey = (RSAPrivateKey)RSA.getUrlPrivateKey(jwtConfig.getPrivateKey());
                Algorithm.RSA256(publicKey,privateKey);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (CertificateException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (InvalidKeySpecException e) {
                LOGGER.error(e.getMessage(),e);
            }
            return null;
        }
    },
    RSA384(){
        @Override
        public Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig) {
            try {
                RSAPublicKey publicKey = (RSAPublicKey)RSA.getUrlPublicKey(jwtConfig.getPublicKey());
                RSAPrivateKey privateKey = (RSAPrivateKey)RSA.getUrlPrivateKey(jwtConfig.getPrivateKey());
                Algorithm.RSA256(publicKey,privateKey);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (CertificateException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (InvalidKeySpecException e) {
                LOGGER.error(e.getMessage(),e);
            }
            return null;
        }
    },
    RSA512(){
        @Override
        public Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig) {
            try {
                RSAPublicKey publicKey = (RSAPublicKey)RSA.getUrlPublicKey(jwtConfig.getPublicKey());
                RSAPrivateKey privateKey = (RSAPrivateKey)RSA.getUrlPrivateKey(jwtConfig.getPrivateKey());
                Algorithm.RSA256(publicKey,privateKey);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (CertificateException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (InvalidKeySpecException e) {
                LOGGER.error(e.getMessage(),e);
            }
            return null;
        }
    },
    HMAC256(){
        @Override
        public Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig) {
            return Algorithm.HMAC256(jwtConfig.getKey());
        }
    },
    HMAC384(){
        @Override
        public Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig) {
            return Algorithm.HMAC384(jwtConfig.getKey());
        }
    },
    HMAC512(){
        @Override
        public Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig) {
            return Algorithm.HMAC512(jwtConfig.getKey());
        }
    },
    ECDSA256(){
        @Override
        public Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig) {
            try {
                ECPublicKey publicKey = RSA.getUrlECPublicKey(jwtConfig.getPublicKey());
                ECPrivateKey privateKey = RSA.getUrlECPrivateKey(jwtConfig.getPrivateKey());
                Algorithm.ECDSA384(publicKey,privateKey);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (CertificateException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (InvalidKeySpecException e) {
                LOGGER.error(e.getMessage(),e);
            }
            return null;
        }
    },
    ECDSA384(){
        @Override
        public Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig) {
            try {
                ECPublicKey publicKey = RSA.getUrlECPublicKey(jwtConfig.getPublicKey());
                ECPrivateKey privateKey = RSA.getUrlECPrivateKey(jwtConfig.getPrivateKey());
                Algorithm.ECDSA384(publicKey,privateKey);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (CertificateException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (InvalidKeySpecException e) {
                LOGGER.error(e.getMessage(),e);
            }
            return null;
        }
    },
    ECDSA512(){
        @Override
        public Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig) {
            try {
                ECPublicKey publicKey = RSA.getUrlECPublicKey(jwtConfig.getPublicKey());
                ECPrivateKey privateKey = RSA.getUrlECPrivateKey(jwtConfig.getPrivateKey());
                Algorithm.ECDSA384(publicKey,privateKey);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (CertificateException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (InvalidKeySpecException e) {
                LOGGER.error(e.getMessage(),e);
            }
            return null;
        }
    };
    abstract Algorithm toAlgorithm(SecurityManagerConfig.Jwt jwtConfig);
    Log LOGGER = LogFactory.getLog(JwtAlgorithm.class);
}
