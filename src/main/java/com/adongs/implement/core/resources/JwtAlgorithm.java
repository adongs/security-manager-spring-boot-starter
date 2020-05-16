package com.adongs.implement.core.resources;

import com.adongs.config.ResourceCertificationConfig;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.util.StringUtils;

/**
 * 暂时只支持HMAC
 * @author yudong
 * @version 1.0
 */
public enum JwtAlgorithm {


    HMAC256(){
        @Override
        public Algorithm toAlgorithm(ResourceCertificationConfig config) {

            return Algorithm.HMAC256(key(config));
        }
    },
    HMAC384(){
        @Override
        public Algorithm toAlgorithm(ResourceCertificationConfig config) {
            return Algorithm.HMAC384(key(config));
        }
    },
    HMAC512(){
        @Override
        public Algorithm toAlgorithm(ResourceCertificationConfig config) {
            return Algorithm.HMAC512(key(config));
        }
    };

  protected String key(ResourceCertificationConfig config){
       if (StringUtils.isEmpty(config.getKey())){
           throw new NullPointerException("The key is empty");
       }
       return config.getKey();
   }
    abstract Algorithm toAlgorithm(ResourceCertificationConfig config);
}
