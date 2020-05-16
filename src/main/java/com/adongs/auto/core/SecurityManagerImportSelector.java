package com.adongs.auto.core;

import com.adongs.annotation.core.EnableSecurityManager;
import com.adongs.auto.core.auto.*;
import com.adongs.constant.Components;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 权限自动配置
 * @author yudong
 * @version 1.0
 */
public class SecurityManagerImportSelector implements ImportSelector {

    private final static Log LOGGER = LogFactory.getLog(SecurityManagerImportSelector.class);

    private final static Map<String,Object> ANNOTATION_ATTRIBUTES = new HashMap<>();

    private final static ImmutableMap<Components,Class<?>> COMPONENTS = ImmutableMap.<Components,Class<?>>builder()
            .put(Components.RESOURCES, ResourcesSecurityAuto.class)
            .put(Components.AUTHENTICATION, AuthoritySecurityAuto.class)
            .put(Components.RATELIMITERS, RatelimitersAuto.class)
            .put(Components.VERSION, VersionAuto.class)
            .put(Components.SIGN, SignAuto.class)
            .put(Components.RESUBMIT,ResubmitAuto.class)
            .build();

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        init(annotationMetadata);
        Boolean requestReaders = attributes("requestReaders", Boolean.class);
        Components[] components = attributesArray("components", Components.class);
        return className(requestReaders,components);
    }

    /**
     * 自动转换
     * @param requestReaders
     * @param components
     * @return
     */
    private String [] className(Boolean requestReaders,Components[] components){
        Set<String> className = Sets.newHashSet();
        if (requestReaders){
            className.add(RepeatReadRequestAuto.class.getName());
        }
        for (int i = 0,l=components.length; i < l; i++) {
            Class<?> component = COMPONENTS.get(components[i]);
            if (component!=null) {
                className.add(component.getName());
            }
        }
        return className.toArray(new String[]{});
    }


    /**
     * 初始化参数
     * @param annotationMetadata
     */
    private void init(AnnotationMetadata annotationMetadata){
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableSecurityManager.class.getName());
        ANNOTATION_ATTRIBUTES.putAll(annotationAttributes);
    }

    /**
     * 获取值
     * @param name 名称
     * @param clazz 类型
     * @param <T> 泛型
     * @return 值
     */
    private <T>T attributes(String name,Class<T> clazz){
        Object o = ANNOTATION_ATTRIBUTES.get(name);
        return clazz.cast(o);
    }

    /**
     * 获取值数组
     * @param name 名称
     * @param clazz 类型
     * @param <T> 泛型
     * @return 值
     */
    private <T>T [] attributesArray(String name,Class<T> clazz){
        Object o = ANNOTATION_ATTRIBUTES.get(name);
        return (T []) o;
    }

}


