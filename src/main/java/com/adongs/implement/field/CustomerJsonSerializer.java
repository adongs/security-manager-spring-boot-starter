package com.adongs.implement.field;

import com.adongs.annotation.extend.ignores.IgnoresField;
import com.adongs.annotation.extend.ignores.IgnoresFields;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * @author yudong
 * @version  1.0
 */
public class CustomerJsonSerializer {

   private final ObjectMapper mapper;
   private final JacksonJsonFilter jacksonFilter = JacksonJsonFilter.build();

    private CustomerJsonSerializer(Annotation[] annos,ObjectMapper mapper) {
        this.mapper = mapper.copy();
        this.auto(annos);
    }

    private CustomerJsonSerializer(Annotation[] annos) {
        this(annos,new ObjectMapper());
    }

    public static CustomerJsonSerializer build(Annotation[] annos){
        return new CustomerJsonSerializer(annos);
    }
    public static CustomerJsonSerializer build(Annotation[] annos,ObjectMapper mapper){
        return new CustomerJsonSerializer(annos,mapper);
    }

    public byte[] toJson(Object object) throws JsonProcessingException {
        mapper.setFilterProvider(jacksonFilter);
        return mapper.writeValueAsBytes(object);
    }

    public <T>T toObject(byte[] bytes,Class<T> clazz) throws IOException {
        mapper.setFilterProvider(jacksonFilter);
        return  mapper.readValue(bytes,clazz);
    }

    public <T>T toObject(T clazz){
        mapper.setFilterProvider(jacksonFilter);
        try {
            byte[] bytes = mapper.writeValueAsBytes(clazz);
           return  (T) mapper.readValue(bytes, clazz.getClass());
        }catch (IOException e){}
       return clazz;
    }


    private void filter(IgnoresField json) {
        jacksonFilter.filter(json.type(),json.filter())
                .include(json.type(),json.include());
        mapper.addMixIn(json.type(), jacksonFilter.getClass());
    }
    private void filters(IgnoresFields jsons) {
        IgnoresField[] value = jsons.value();
        for (IgnoresField ignoresField : value) {
            filter(ignoresField);
        }
    }

    private void auto(Annotation[] annos){
        for (Annotation a : annos) {
            if (a instanceof IgnoresField) {
                IgnoresField json = (IgnoresField) a;
                filter(json);
            } else if (a instanceof IgnoresFields) {
                IgnoresFields jsons = (IgnoresFields) a;
                filters(jsons);
            }
        }
    }



}
