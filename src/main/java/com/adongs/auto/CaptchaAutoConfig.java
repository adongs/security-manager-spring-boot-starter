package com.adongs.auto;

import com.adongs.config.CaptchaGlobalConfig;
import com.adongs.implement.captcha.CaptchaAspect;
import com.adongs.implement.captcha.cache.MemoryCaptchaCache;
import com.adongs.implement.captcha.marker.MarkerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author yudong
 * @version 1.0
 */
public class CaptchaAutoConfig {

    @Bean
    public MemoryCaptchaCache memoryCaptchaCache(CaptchaGlobalConfig config){
      return new MemoryCaptchaCache(config);
    }

    @Bean
    public MarkerFactory markerFactory(MemoryCaptchaCache cache,CaptchaGlobalConfig config){
        return  new MarkerFactory(cache,config);
    }

    @Bean
    public CaptchaAspect captchaAspect(){
        return new CaptchaAspect();
    }

}
