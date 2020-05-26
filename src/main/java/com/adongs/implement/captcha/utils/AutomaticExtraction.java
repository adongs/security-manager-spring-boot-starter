package com.adongs.implement.captcha.utils;

import com.adongs.implement.captcha.model.ResolveModel;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;

/**
 * 自动提取
 * @author yudong
 * @version 1.0
 */
public class AutomaticExtraction {


    /**
     * 提取规则
     * @param param 方法参数
     * @param keyName 提取字段名称
     * @return 提取规则
     */
    public LinkedList<ResolveModel> extract(Map<String,Object> param, String keyName){
        LinkedList<ResolveModel> linkedLists = new LinkedList();
        for (Map.Entry<String, Object> stringObjectEntry : param.entrySet()) {
            final String key = stringObjectEntry.getKey();
            if (keyName.equals(key)){
                linkedLists.addLast(new ResolveModel(null,keyName));
               return linkedLists;
            }
            final Object value = stringObjectEntry.getValue();
            final boolean extract = extract(linkedLists, value.getClass(), keyName);
            if (extract){
                linkedLists.addLast(new ResolveModel(null,key));
                return linkedLists;
            }
        }
       return null;
    }

    /**
     * 递归查找字段名称
     * @param clazz 类型
     * @param keyName 字段名称
     * @return true 搜索到  flase未搜索到
     */
   private boolean extract(LinkedList<ResolveModel> resolveModels,Class<?> clazz,String keyName){
       final boolean isDefined = clazz.getClassLoader() != null;
       if (isDefined){
           final Field[] fields = clazz.getDeclaredFields();
           for (Field field : fields) {
               final boolean equals = field.getName().equals(keyName);
               if (equals){
                   field.setAccessible(true);
                   resolveModels.addLast(new ResolveModel(field,field.getName()));
                   return true;
               }else{
                   final boolean isCustom = field.getType().getClassLoader() != null;
                    if (isCustom){
                        final boolean extract = extract(resolveModels, field.getType(), keyName);
                        if (extract){
                            field.setAccessible(true);
                            resolveModels.addLast(new ResolveModel(field,field.getName()));
                            return true;
                        }
                    }
               }
           }
       }
       return false;
   }


    /**
     * 解析规则
     * @param params 参数
     * @param rule 规则名称
     * @return 解析结果
     */
   public Object resolve(Map<String,Object> params, LinkedList<ResolveModel> rule){
       rule = new LinkedList(rule);
       final ResolveModel resolveModel = rule.pollLast();
       final Object o = params.get(resolveModel.getName());
       if (rule.peekLast()!=null) {
           return resolve(o, rule);
       }
       return o;
   }

    /**
     * 递归解析
     * @param param 参数
     * @param rule 规则
     * @return 解析
     */
   private Object resolve(Object param,LinkedList<ResolveModel> rule){
       final ResolveModel resolveModel = rule.pollLast();
       final Object object = ReflectionUtils.getField(resolveModel.getField(), param);
       if (rule.peekLast()==null){
           return object;
       }
       return resolve(object,rule);
   }
}
