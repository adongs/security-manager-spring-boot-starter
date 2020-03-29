package com.adongs.implement;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BaseAspect {


    /**
     * 获取注解
     * @param joinPoint JoinPoint
     * @param clazz 获取注解类型
     * @param <T> 泛型
     * @return 指定注解
     */
    public <T extends Annotation>T getAnnotation(JoinPoint joinPoint, Class<T> clazz){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(clazz);
    }

    /**
     * 获取方法参数
     * @param joinPoint JoinPoint
     * @return 返回这个方法的参数
     */
    public Map<String,Object> getParams(JoinPoint joinPoint){
        Map<String,Object> paramAndValue = new HashMap<>();
        MethodSignature methodSignature =  (MethodSignature)joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            paramAndValue.put(parameterNames[i],args[i]);
        }
        return paramAndValue;
    }


}
