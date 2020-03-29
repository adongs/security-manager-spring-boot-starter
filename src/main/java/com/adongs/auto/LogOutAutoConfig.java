package com.adongs.auto;

import com.adongs.implement.logout.DefaultLogOutFormat;
import org.springframework.context.annotation.Bean;

/**
 * 日志自动配置
 * @author yudong
 * @version 1.0
 */

public class LogOutAutoConfig {

    /**
     * 自动配置格式化
     * @param securityManagerConfig 配置参数
     * @return 自动配置格式化
     */
    @Bean
    public DefaultLogOutFormat defaultLogOutFormat(SecurityManagerConfig securityManagerConfig){
        SecurityManagerConfig.Logs log = securityManagerConfig.getLog();
        DefaultLogOutFormat defaultLogFormat = new DefaultLogOutFormat(log.getLineFeed());
        return defaultLogFormat;
    }


}
