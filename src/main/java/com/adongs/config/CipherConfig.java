package com.adongs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 密码配置
 * @author yudong
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.cipher")
public class CipherConfig {

    /**
     * 默认处理器
     */
    private String processor = "default";

    private Codings des = new  Codings();

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public Codings getDes() {
        return des;
    }

    public void setDes(Codings des) {
        this.des = des;
    }

    public static class Codings{
        /**
         * 秘钥
         */
        private String key;

        /**
         * 偏移量
         */
        private String offset;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getOffset() {
            return offset;
        }

        public void setOffset(String offset) {
            this.offset = offset;
        }
    }
}
