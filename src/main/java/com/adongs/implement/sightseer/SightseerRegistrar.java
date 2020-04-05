package com.adongs.implement.sightseer;

import com.adongs.annotation.core.Certification;
import com.adongs.annotation.core.Sightseer;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 注解 Sightseer 扫描
 * @author yudong
 * @version 1.0
 */
public class SightseerRegistrar  implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        ApplicationContext parent = applicationContext.getParent();
        if (parent!=null){return;}
        Map<String, Object> restController = applicationContext.getBeansWithAnnotation(RestController.class);
        Map<String, Object> controller = applicationContext.getBeansWithAnnotation(Controller.class);
        restController.putAll(controller);
        Set<String> strings = sightseerUrl(restController.values());
        applicationContext.publishEvent(new SightseerEven(strings));
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
     * 获取类上忽略url
     * @param collection collection遍历实体类
     * @return 忽略的url
     */
    private Set<String> sightseerUrl(Collection<Object> collection){
        Set<String> sightseerUrl = Sets.newHashSet();
        Iterator<Object> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object next = getTargetBean(iterator.next());
            Set<String> prefixs = Sets.newHashSet();
            RequestMapping requestMapping = next.getClass().getAnnotation(RequestMapping.class);
            if (requestMapping!=null){
                prefixs.addAll(Sets.newHashSet(requestMapping.value()));
            }
            Sightseer annotation =next.getClass().getAnnotation(Sightseer.class);
            Set<String> methodSightseerUrl = annotation==null?getMethodSightseerUrl(next):getClassSightseerUrl(next);
            boolean empty = prefixs.isEmpty();
            if (empty){
                sightseerUrl.addAll(methodSightseerUrl);
            }else{
                prefixs.forEach(p->{
                    methodSightseerUrl.forEach(m->{
                        String url = urlConstruct(p,m);
                        sightseerUrl.add(url);
                    });
                });
            }
         }
      return sightseerUrl;
    }




    /**
     * 获取方法上的忽略url
     * @param object 实体类
     * @return 忽略的url
     */
   private Set<String> getMethodSightseerUrl(Object object){
       Set<String> sightseerUrl = Sets.newHashSet();
       Method[] methods = object.getClass().getMethods();
       for (int i = 0,l=methods.length; i < l; i++) {
           Method method = methods[i];
           Sightseer sightseer = method.getAnnotation(Sightseer.class);
           Certification certification = method.getAnnotation(Certification.class);
           if (sightseer!=null && certification==null){
               RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
               if (requestMapping!=null){
                   sightseerUrl.addAll(Sets.newHashSet(requestMapping.value()));
                   continue;
               }
               PostMapping  postMapping= method.getAnnotation(PostMapping.class);
               if (postMapping!=null){
                   sightseerUrl.addAll(Sets.newHashSet(postMapping.value()));
                   continue;
               }
               GetMapping  getMapping= method.getAnnotation(GetMapping.class);
               if (getMapping!=null){
                   sightseerUrl.addAll(Sets.newHashSet(getMapping.value()));
                   continue;
               }
               DeleteMapping deleteMapping= method.getAnnotation(DeleteMapping.class);
               if (deleteMapping!=null){
                   sightseerUrl.addAll(Sets.newHashSet(deleteMapping.value()));
                   continue;
               }
               PatchMapping patchMapping= method.getAnnotation(PatchMapping.class);
               if (patchMapping!=null){
                   sightseerUrl.addAll(Sets.newHashSet(patchMapping.value()));
                   continue;
               }
               PutMapping putMapping= method.getAnnotation(PutMapping.class);
               if (putMapping!=null){
                   sightseerUrl.addAll(Sets.newHashSet(putMapping.value()));
                   continue;
               }
           }
       }
       return sightseerUrl;
   }

    /**
     * 获取类上的忽略url
     * @param object 实体
     * @return 忽略的url
     */
    private Set<String> getClassSightseerUrl(Object object){
        Set<String> sightseerUrl = Sets.newHashSet();
        Certification certificationClass = object.getClass().getAnnotation(Certification.class);
        boolean isCertificationClassNull = certificationClass==null?true:false;
        Method[] methods = object.getClass().getMethods();
        for (int i = 0,l=methods.length; i < l; i++) {
            Method method = methods[i];
            Certification certification = method.getAnnotation(Certification.class);
            if (certification==null && isCertificationClassNull){
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping!=null){
                    sightseerUrl.addAll(Sets.newHashSet(requestMapping.value()));
                    continue;
                }
                PostMapping  postMapping= method.getAnnotation(PostMapping.class);
                if (postMapping!=null){
                    sightseerUrl.addAll(Sets.newHashSet(postMapping.value()));
                    continue;
                }
                GetMapping  getMapping= method.getAnnotation(GetMapping.class);
                if (getMapping!=null){
                    sightseerUrl.addAll(Sets.newHashSet(getMapping.value()));
                    continue;
                }
                DeleteMapping deleteMapping= method.getAnnotation(DeleteMapping.class);
                if (deleteMapping!=null){
                    sightseerUrl.addAll(Sets.newHashSet(deleteMapping.value()));
                    continue;
                }
                PatchMapping patchMapping= method.getAnnotation(PatchMapping.class);
                if (patchMapping!=null){
                    sightseerUrl.addAll(Sets.newHashSet(patchMapping.value()));
                    continue;
                }
                PutMapping putMapping= method.getAnnotation(PutMapping.class);
                if (putMapping!=null){
                    sightseerUrl.addAll(Sets.newHashSet(putMapping.value()));
                    continue;
                }
            }
        }
        return sightseerUrl;
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
