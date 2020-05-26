package com.adongs.implement.captcha.processor;

import com.adongs.annotation.core.Captcha;
import com.adongs.config.CaptchaGlobalConfig;
import com.adongs.constant.CodeType;
import com.adongs.implement.captcha.model.CaptchaConfig;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 收集Captcha 注解信息
 * 通过事件负责通知 图片验证处理器(CaptchaProcessor)准备就绪
 * @author yudong
 * @version 1.0
 */
@Component
public class CaptchaCollector implements ApplicationListener<ApplicationStartedEvent> {

    private ConfigurableApplicationContext context;

    @Autowired
    private CaptchaGlobalConfig globalConfig;

    @Autowired
    private CaptchaProcessor captchaProcessor;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
       this.context = event.getApplicationContext();
        Map<String, Object> controller = this.context.getBeansWithAnnotation(Controller.class);
        Map<String, Object> restController = this.context.getBeansWithAnnotation(RestController.class);
        controller.putAll(restController);
        final Map<String, CaptchaConfig> captchaConfigMap = captchaInfo(controller);
        captchaProcessor.addConfig(captchaConfigMap);
    }

    private Map<String,CaptchaConfig> captchaInfo(Map<String, Object> controller){
        Map<String,CaptchaConfig> captchaConfigMap = Maps.newHashMap();
        final Iterator<Object> iterator = controller.values().iterator();
        while (iterator.hasNext()){
            Object next = getTargetBean(iterator.next());
            final Method[] methods = next.getClass().getMethods();
            for (int i = 0,l=methods.length; i < l; i++) {
                final Method method = methods[i];
                final Captcha captcha = method.getAnnotation(Captcha.class);
                if (captcha!=null){
                    final Set<String> url = getUrl(getClassUrl(next), method,captcha);
                    url.forEach(u->{
                        final CaptchaConfig construct = construct(captcha);
                        captchaConfigMap.put(u,construct);
                    });
                }
            }
        }
        return captchaConfigMap;
    }
    /**
     * 构建二维码配置信息
     * @param captcha
     * @return
     */
   private CaptchaConfig construct(Captcha captcha){
      CaptchaConfig captchaConfig = new CaptchaConfig();
      if(StringUtils.isEmpty(captcha.requestId())){
          captchaConfig.setRequestId(globalConfig.getRequestId());
      }else{
          captchaConfig.setRequestId(captcha.requestId());
      }
      if (StringUtils.isEmpty(captcha.widthHeight())){
          final String[] xes = globalConfig.getWidthHeight().split("X");
          captchaConfig.setWidth(Integer.valueOf(xes[0]));
          captchaConfig.setHeight(Integer.valueOf(xes[1]));
      }else{
          final String[] xes = captcha.widthHeight().split("X");
          captchaConfig.setWidth(Integer.valueOf(xes[0]));
          captchaConfig.setHeight(Integer.valueOf(xes[1]));
      }
      if (StringUtils.isEmpty(captcha.digits())){
          captchaConfig.setDigits(Integer.valueOf(globalConfig.getDigits()));
      }else{
          captchaConfig.setDigits(Integer.valueOf(captcha.digits()));
      }
      if(captcha.characterType()==-1){
          captchaConfig.setCharacterType(globalConfig.getCharacterType());
      }else{
          captchaConfig.setCharacterType(captcha.characterType());
      }
      if (captcha.codeType()== CodeType.CONFIG){
          final CodeType codeType = globalConfig.getCodeType();
          if (codeType == CodeType.CONFIG){
              globalConfig.setCodeType(CodeType.GIF);
          }
          captchaConfig.setCodeType(globalConfig.getCodeType());
      }else{
          captchaConfig.setCodeType(captcha.codeType());
      }
      if (captcha.font()==-1){
          captchaConfig.setFont(globalConfig.getFont());
      }else{
          captchaConfig.setFont(captcha.font());
      }
      return captchaConfig;
  }

   private Set<String> getClassUrl(Object object){
       final RequestMapping controller = object.getClass().getAnnotation(RequestMapping.class);
       if (controller!=null){
            return Sets.newHashSet(controller.value());
       }
       return Sets.newHashSet();
   }

   private Set<String> getMethodsUrl(Method method){
       RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
       if (requestMapping!=null){
           return Sets.newHashSet(requestMapping.value());
       }
       PostMapping postMapping= method.getAnnotation(PostMapping.class);
       if (postMapping!=null){
           return Sets.newHashSet(postMapping.value());
       }
       GetMapping getMapping= method.getAnnotation(GetMapping.class);
       if (getMapping!=null){
           return Sets.newHashSet(getMapping.value());
       }
       DeleteMapping deleteMapping= method.getAnnotation(DeleteMapping.class);
       if (deleteMapping!=null){
           return Sets.newHashSet(deleteMapping.value());
       }
       PatchMapping patchMapping= method.getAnnotation(PatchMapping.class);
       if (patchMapping!=null){
           return Sets.newHashSet(patchMapping.value());
       }
       PutMapping putMapping= method.getAnnotation(PutMapping.class);
       if (putMapping!=null){
           return Sets.newHashSet(putMapping.value());
       }
       return Sets.newHashSet();
   }

   private Set<String> getUrl(Set<String> classUrls,Method method,Captcha captcha){
       final Set<String> methodsUrls = getMethodsUrl(method);
       Set<String> url = Sets.newHashSet();
       for (String methodsUrl : methodsUrls) {
           if (classUrls.isEmpty()){
               url.add(urlConstruct(methodsUrl));
               continue;
           }
           for (String classUrl : classUrls) {
               url.add(urlConstruct(classUrl,methodsUrl,captcha.value()));
           }
       }
       return url;
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
}
