package com.security.manager.implement.decrypt;


import com.security.manager.annotation.extend.Decode;
import com.security.manager.exception.DecodeException;
import com.security.manager.implement.BaseAspect;
import com.security.manager.utils.el.ElAnalysis;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 解密
 */
@Aspect
@Component
public class DecodeAspect extends BaseAspect {



    @Autowired
    private ApplicationContext applicationContext;

    @Around(value = "@annotation(decode)")
    public Object before(ProceedingJoinPoint joinPoint, Decode decode)throws Throwable{
        DecryptProcessor bean = applicationContext.getBean(decode.processor());
        if (bean==null){
           return joinPoint.proceed(joinPoint.getArgs());
        }
        Map<String, Object> params = getParams(joinPoint);
        ElAnalysis elAnalysis = new ElAnalysis(params);
        String[] values = decode.values();
        for (String value : values) {
            if (!elAnalysis.type(value).getClass().equals(String.class.getClass())){
               throw new DecodeException("Type is not a string");
            }
            String analysis = elAnalysis.analysis(value);
            String encryption = bean.decode(analysis);
            elAnalysis.reinstall(value,encryption);
            boolean layer = isLayer(value);
            if (layer){
                String fieldName = fieldName(value);
                boolean containsKey = params.containsKey(fieldName);
                if (containsKey){
                    params.put(fieldName,encryption);
                }
            }
        }
       return joinPoint.proceed(params.values().toArray());
    }


    /**
     * 判断是否为一层
     * @return
     */
    private boolean isLayer(String el){
        return el.indexOf(".")==-1;
    }

    /**
     * 获取字段名称
     * @param el
     * @return
     */
    private String fieldName(String el){
        return el.substring(el.indexOf("#")+1);
    }

}
