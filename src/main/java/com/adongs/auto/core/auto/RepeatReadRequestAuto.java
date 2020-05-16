package com.adongs.auto.core.auto;

import com.adongs.session.filter.BodyRepeatedReadingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * body重复读取
 * @author yudong
 * @version 1.0
 */
public class RepeatReadRequestAuto {

    /**
     * 配置重复读取
     * @return  配置重复读取Filter
     */
    @Bean
    public FilterRegistrationBean readerfilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        BodyRepeatedReadingFilter bodyReaderFilter = new BodyRepeatedReadingFilter();
        filterRegistrationBean.setFilter(bodyReaderFilter);
        filterRegistrationBean.setOrder(0);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


}
