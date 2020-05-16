package com.adongs.session.aggregator;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 权限获取事件
 * @author yudong
 * @version 1.0
 */
public class AuthorityEvent extends ApplicationEvent {

    public AuthorityEvent(Map<String, UrlAuthority> urlAuthorityMap) {
        super(urlAuthorityMap);
    }

    /**
     * 转换数据
     * @return 数据
     */
    public Map<String,UrlAuthority> get(){
       return (Map<String,UrlAuthority>)this.getSource();
    }
}
