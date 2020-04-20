package com.adongs.auto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "spring.security.manager")
public class SecurityManagerConfig {

    /**
     * 请求
     */
    private Request request = new Request();
    /**
     * des加密
     */
    private Codings des = new Codings();
    /**
     * 日志
     */
    private Logs log = new Logs();

    /**
     * 分布式互斥锁 redis实现,效率高
     */
    private Lock redis = new Lock();
    /**
     * 分布式互斥锁 zookeeper实现,可靠性高
     */
    private Lock zookeeper = new Lock();

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Codings getDes() {
        return des;
    }

    public void setDes(Codings des) {
        this.des = des;
    }

    public Logs getLog() {
        return log;
    }

    public void setLog(Logs log) {
        this.log = log;
    }

    public Lock getRedis() {
        return redis;
    }

    public void setRedis(Lock redis) {
        this.redis = redis;
    }

    public Lock getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(Lock zookeeper) {
        this.zookeeper = zookeeper;
    }

    public static class Logs{
        /**
         * 日志单行模式
         */
        private Boolean lineFeed = true;

        public Boolean getLineFeed() {
            return lineFeed;
        }

        public void setLineFeed(Boolean lineFeed) {
            this.lineFeed = lineFeed;
        }
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

    public static class Resubmit{
        /**
         * 是否开启重复提交
         */
        private boolean enabled = true;
        /**
         * 初始化重复提交容器的数量
         */
        private int initCount = 2048;
        /**
         * 过期时间单位毫秒
         */
        private long duration=10000;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getInitCount() {
            return initCount;
        }

        public void setInitCount(int initCount) {
            this.initCount = initCount;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }

    public static class Request{
        /**
         * 是否开启权限
         */
        private boolean enabled = true;
        /**
         * 请求token的key,在header中获取
         */
        private String token = "token";
        /**
         * 游客模式
         * 为true时 token为null时允许访问
         * 为false时 只有用@Sightseer标注的controller才可以访问
         */
        private boolean tourists = false;
        /**
         * 请求版本号的key,在header中获取
         */
        private String version = "version";
        /**
         * 请求时间的key,在header中获取
         */
        private String timestamp = "timestamp";
        /**
         * 盐值的key,在header中获取
         */
        private String sign = "sign";
        /**
         * 是否开启 request重复读取
         */
        private boolean readers = true;
        /**
         * 限流数据,每秒访问量
         */
        private Double permitsPerSecond;
        /**
         * 忽略验证token的url
         */
        private Set<String> ignoreUrl;
        /**
         * 重复提交
         */
        private Resubmit resubmit = new Resubmit();

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public boolean getTourists() {
            return tourists;
        }

        public void setTourists(boolean tourists) {
            this.tourists = tourists;
        }

        public boolean isReaders() {
            return readers;
        }

        public void setReaders(boolean readers) {
            this.readers = readers;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Double getPermitsPerSecond() {
            return permitsPerSecond;
        }

        public void setPermitsPerSecond(Double permitsPerSecond) {
            this.permitsPerSecond = permitsPerSecond;
        }

        public Resubmit getResubmit() {
            return resubmit;
        }

        public void setResubmit(Resubmit resubmit) {
            this.resubmit = resubmit;
        }

        public Set<String> getIgnoreUrl() {
            return ignoreUrl;
        }

        public void setIgnoreUrl(Set<String> ignoreUrl) {
            this.ignoreUrl = ignoreUrl;
        }
    }

    public static class Lock{
        /**
         * 开启分布式互斥锁
         */
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
