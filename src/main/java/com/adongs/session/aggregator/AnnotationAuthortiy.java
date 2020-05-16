package com.adongs.session.aggregator;

import com.adongs.annotation.core.Authentication;
import com.adongs.annotation.core.Sightseer;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 扫描注解权限
 * @author yudong
 * @version 1.0
 */
@Component
public class AnnotationAuthortiy extends CollectionAuthortiy{

    public AnnotationAuthortiy() {
        super(null);
    }

    @Override
    public Map<String, UrlAuthority> collection() {
        Map<String, UrlAuthority> map = Maps.newHashMap();
        final ApplicationContext context = context();
        Map<String, Object> controller = context.getBeansWithAnnotation(Controller.class);
        Map<String, Object> restController = context.getBeansWithAnnotation(RestController.class);
        controller.putAll(restController);
        classInfo(controller.values(),map);
        return map;
    }


    /**
     * 采集类上的信息
     * @param collection controller对象
     * @param map 收集数据集
     */
    public void classInfo(Collection<Object> collection, Map<String, UrlAuthority> map){
        final Iterator<Object> iterator = collection.iterator();
         while (iterator.hasNext()){
             Object next = getTargetBean(iterator.next());
             RequestMapping requestMapping = next.getClass().getAnnotation(RequestMapping.class);
             Sightseer sightseer = next.getClass().getAnnotation(Sightseer.class);
             Authentication authentication = next.getClass().getAnnotation(Authentication.class);
             methodInfo(next,requestMapping!=null?requestMapping.value():null,sightseer,authentication,map);
         }
    }

    /**
     * 采集方法上的数据
     * @param next 原始类
     * @param urlPrefix controller类上的路径地址
     * @param sightseer 忽略注解
     * @param authentication 类上的权限
     * @param map 权限容器
     */
    public void methodInfo(Object next,String [] urlPrefix,Sightseer sightseer,Authentication authentication,Map<String, UrlAuthority> map){
        final Method[] methods = next.getClass().getMethods();
        for (int i = 0,l=methods.length; i < l; i++) {
            final Method method = methods[i];
            final String[] methodUrl = methodUrl(method);
            if (methodUrl!=null && methodUrl.length>0){
                final Set<String> combination = combinationUrl(urlPrefix, methodUrl);
                final Authentication methodAuthentication = method.getAnnotation(Authentication.class);
                final Sightseer methodSightseer = method.getAnnotation(Sightseer.class);
                if (methodAuthentication==null && methodSightseer!=null){
                    UrlAuthority authority = new UrlAuthority(true);
                    combination.forEach(u->{map.put(u,authority);});
                    continue;
                }
                if (methodAuthentication == null && authentication==null && sightseer!=null){
                    UrlAuthority authority = new UrlAuthority(true);
                    combination.forEach(u->{map.put(u,authority);});
                    continue;
                }
            }
        }
    }


    /**
     * 获取方法上的url
     * @param method
     * @return url
     */
    private String[] methodUrl(Method method){
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping!=null){
           return requestMapping.value();
        }
        PostMapping postMapping= method.getAnnotation(PostMapping.class);
        if (postMapping!=null){
            return postMapping.value();
        }
        GetMapping getMapping= method.getAnnotation(GetMapping.class);
        if (getMapping!=null){
            return getMapping.value();
        }
        DeleteMapping deleteMapping= method.getAnnotation(DeleteMapping.class);
        if (deleteMapping!=null){
            return deleteMapping.value();
        }
        PatchMapping patchMapping= method.getAnnotation(PatchMapping.class);
        if (patchMapping!=null){
            return patchMapping.value();
        }
        PutMapping putMapping= method.getAnnotation(PutMapping.class);
        if (putMapping!=null){
            return putMapping.value();
        }
        return null;
    }


    /**
     * 组合url
     * @param urlPrefix 类上的url
     * @param urlSuffix 方法上的url
     * @return 组合后的url
     */
    public Set<String> combinationUrl(String [] urlPrefix,String [] urlSuffix){
          if (urlPrefix==null || urlPrefix.length==0){
              if (urlSuffix!=null && urlSuffix.length>0){
                  return Sets.newHashSet(urlSuffix);
              }else{
                  return Sets.newHashSet();
              }
          }else{
              if (urlSuffix!=null && urlSuffix.length>0){
                   HashSet<String> set = Sets.newHashSet();
                  for (int i = 0,l=urlPrefix.length; i < l; i++) {
                      for (int i1 = 0,l1=urlSuffix.length; i1 < l1; i1++) {
                          set.add(urlConstruct(urlPrefix[i],urlSuffix[i1]));
                      }
                   }
                   return set;
              }else{
                  return Sets.newHashSet(urlPrefix);
              }
          }
    }

    /**
     * 提取信息
     * @param authentication
     * @return
     */
    private UrlAuthority urlAuthority(Authentication authentication){
        UrlAuthority authority = new UrlAuthority(
                Sets.newHashSet(authentication.roles()),
                authentication.rlogical(),
                Sets.newHashSet(authentication.permissions()),
                authentication.plogical()
        );
        return authority;
    }


    /**
     * 获取被代理后的原始类
     * @param bean
     * @return
     */
    private Object getTargetBean(Object bean){
        if (AopUtils.isAopProxy(bean)){
            try{
                Object target = ((Advised) bean).getTargetSource().getTarget();
                return target;
            }catch (Exception e){
                return bean;
            }
        }
        return bean;
    }

    /**
     * url合法处理
     * @param urlFragment url数组
     * @return url合法处理
     */
    private String urlConstruct(String ... urlFragment){
        StringBuilder url = new StringBuilder();
        for (int i = 0,l=urlFragment.length; i < l; i++) {
            url.append("/").append(urlFragment[i]);
        }
        String replace = StringUtils.replace(url.toString(), "///", "/");
        replace = StringUtils.replace(replace, "//", "/");
        int indexOf = replace.indexOf("/");
        if (indexOf==1){
            replace= replace.substring(1);
        }
        return replace;
    }
}
