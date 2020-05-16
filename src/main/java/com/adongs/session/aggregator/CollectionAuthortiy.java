package com.adongs.session.aggregator;

import com.adongs.config.AuthenticationConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.util.Map;

/**
 * 采集权限
 * @author yudong
 * @version 1.0
 */
public abstract class CollectionAuthortiy implements ApplicationListener<ApplicationStartedEvent>{

    private final AuthenticationConfig config;
    private ApplicationContext applicationContext;

    public CollectionAuthortiy(AuthenticationConfig config) {
        this.config = config;
    }

    /**
     * 当项目启动后触发权限采集
     * @param applicationStartedEvent 启动事件监听器
     */
    @Override
    public final void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        applicationContext = applicationStartedEvent.getApplicationContext();
        final Map<String, UrlAuthority> collection = collection();
        if (collection==null || collection.isEmpty()){
            return;
        }
       this.applicationContext.publishEvent(new AuthorityEvent(collection));
    }

    /**
     * 刷新权限配置信息
     */
    public final void refresh(){
        final Map<String, UrlAuthority> collection = collection();
        if (collection==null || collection.isEmpty()){
            return;
        }
        this.applicationContext.publishEvent(new AuthorityEvent(collection));
    }

    /**
     * 采集数据,包装成事件
     * @return 权限事件
     */
    public abstract Map<String, UrlAuthority> collection();

    /**
     * 获取配置
     * @return 配置信息
     */
    public AuthenticationConfig config(){
        AuthenticationConfig config = new AuthenticationConfig();
        BeanUtils.copyProperties(this.config,config);
        return config;
    }

    /**
     * 获取到上下文
     * @return 上下文
     */
    public ApplicationContext context(){
        return applicationContext;
    }

}
