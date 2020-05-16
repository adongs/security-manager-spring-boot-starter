package com.adongs.annotation.core;

import com.adongs.auto.core.SecurityManagerImportSelector;
import com.adongs.constant.Components;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动安全管理
 * @author yudong
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SecurityManagerImportSelector.class})
public @interface EnableSecurityManager {
    /**
     * 是否开启Request重复读取
     * @return true 开启  false关闭
     */
    boolean requestReaders() default false;

    /**
     * 启动相关组件 默认只启用用户权限和资源授权
     * 组件: 资源授权  盐值校验 防止重复提交  版本校验  限流  用户权限
     * @return 组件数组
     */
    Components[] components() default {Components.AUTHENTICATION,Components.RESOURCES};


}
