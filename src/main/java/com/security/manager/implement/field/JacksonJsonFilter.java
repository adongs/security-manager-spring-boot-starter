package com.security.manager.implement.field;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * @Author yudong
 * @Date 2019/7/15 上午11:13
 * @Version 1.0
 */
@JsonFilter("JacksonFilter")
public class JacksonJsonFilter extends FilterProvider {
   private final Map<Class<?>, Set<String>> includeMap = new HashMap<>();
   private final Map<Class<?>, Set<String>> filterMap = new HashMap<>();

   private JacksonJsonFilter(){}

   public static JacksonJsonFilter build(){
       return new JacksonJsonFilter();
   }

    public JacksonJsonFilter include(Class<?> type, String[] fields) {
        if(type!=null && fields.length>0) {
            includeMap.put(type, Sets.newHashSet(fields));
        }
        return this;
    }

    public JacksonJsonFilter filter(Class<?> type, String[] fields) {
        if(type!=null && fields.length>0) {
            filterMap.put(type, Sets.newHashSet(fields));
        }
        return this;
    }

    @Override
    @Deprecated
    @SuppressWarnings({"deprecation","unchecked"})
    public BeanPropertyFilter findFilter(Object filterId) {
        throw new UnsupportedOperationException("Access to deprecated filters not supported");
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {
        return new SimpleBeanPropertyFilter() {

            @Override
            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov, PropertyWriter writer)
                    throws Exception {
                if (apply(pojo.getClass(), writer.getName())) {
                    writer.serializeAsField(pojo, jgen, prov);
                } else if (!jgen.canOmitFields()) {
                    writer.serializeAsOmittedField(pojo, jgen, prov);
                }
            }
        };
    }

    private boolean apply(Class<?> type, String name) {
        Set<String> includeFields = includeMap.get(type);
        Set<String> filterFields = filterMap.get(type);
        if (includeFields != null && includeFields.contains(name)) {
            return true;
        } else if (filterFields != null && !filterFields.contains(name)) {
            return true;
        } else if (includeFields == null && filterFields == null) {
            return true;
        }
        return false;
    }
}
