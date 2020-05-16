package com.adongs.auto.core.auto;

import com.adongs.implement.core.VersionAspect;
import org.springframework.context.annotation.Bean;

/**
 * 版本控制
 * @author yudong
 * @version 1.0
 */
public class VersionAuto {

    @Bean
    public VersionAspect versionAspect(){
        return new VersionAspect();
    }
}
