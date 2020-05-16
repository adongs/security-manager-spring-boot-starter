package com.adongs.implement;

import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Map;

/**
 * 抽象主要功能
 * @author yudong
 * @version 1.0
 */
public class AbstractContextAspect<T extends BaseProcessor> extends BaseAspect {

    private final ApplicationContext applicationContext;

    private final Class<T> clazz;

    private Map<String,T> processor = null;

    public AbstractContextAspect(ApplicationContext applicationContext, Class<T> clazz) {
        this.applicationContext = applicationContext;
        this.clazz = clazz;
    }

    /**
     * 获取处理器
     * @param name 名称
     * @param defaultName 默认名称
     * @return  处理器
     */
    public T processor(String name, String defaultName){
        if (processor==null){
            processor = Maps.newHashMap();
            final Map<String, T> beansOfType = applicationContext.getBeansOfType(clazz);
            final Collection<T> values = beansOfType.values();
            for (T value : values) {
                processor.put(value.name(),value);
            }
        }
      return processor.getOrDefault(name,processor.get(defaultName));
    }






}
