package com.security.manager.auto;

import com.security.manager.implement.logout.DefaultLogOutFormat;
import org.springframework.context.annotation.Bean;

/**
 * @author yudong
 * @version 1.0
 * @date 2020/3/28 10:04 下午
 * @modified By
 */

public class LogOutAutoConfig {

    @Bean
    public DefaultLogOutFormat defaultLogOutFormat(SecurityManagerConfig securityManagerConfig){
        SecurityManagerConfig.Logs log = securityManagerConfig.getLog();
        DefaultLogOutFormat defaultLogFormat = new DefaultLogOutFormat(log.getLineFeed());
        return defaultLogFormat;
    }


}
