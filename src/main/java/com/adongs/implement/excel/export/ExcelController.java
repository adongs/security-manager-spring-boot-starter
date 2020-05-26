package com.adongs.implement.excel.export;

import com.adongs.annotation.extend.excel.ResponseExcel;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 负责构建ExcelController
 * @author yudong
 * @version 1.0
 */
@Component
public class ExcelController implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        final ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        final Map<String, RequestMappingInfoHandlerMapping> beansOfType = applicationContext.getBeansOfType(RequestMappingInfoHandlerMapping.class);
        final RequestMappingInfoHandlerMapping handlerMapping = beansOfType.get("requestMappingHandlerMapping");
        excelController(applicationContext,handlerMapping);
    }


    private void excelController(ApplicationContext context,RequestMappingInfoHandlerMapping handlerMapping){
        Map<String, Object> controller = context.getBeansWithAnnotation(Controller.class);
        Map<String, Object> restController = context.getBeansWithAnnotation(RestController.class);
        controller.putAll(restController);
        for (Object value : controller.values()) {
            final Object targetBean = getTargetBean(value);
            RequestMapping requestMapping = targetBean.getClass().getAnnotation(RequestMapping.class);
            final Method[] methods = targetBean.getClass().getMethods();
            for (int i = 0,l=methods.length; i < l; i++) {
                final Method method = methods[i];
                final ResponseExcel responseExcel = method.getAnnotation(ResponseExcel.class);
                if (responseExcel!=null && !StringUtils.isEmpty(responseExcel.path())){
                    final String[] methodUrl = methodUrl(method);
                    final Set<String> urls = combinationUrl(requestMapping == null ? null : requestMapping.value(), methodUrl);
                    final String[] strings = urls.stream().map(s -> urlConstruct(s, responseExcel.path())).collect(Collectors.toSet()).toArray(new String[]{});
                    final RequestMethod[] requestMethods = requestMethod(method);
                    RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(strings)
                            .methods(requestMethods)
                            .build();
                    handlerMapping.registerMapping(requestMappingInfo,targetBean,method);
                }
            }
        }
    }


    /**
     * 获取方法上的url
     * @param method
     * @return url
     */
    private RequestMethod [] requestMethod(Method method){
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping!=null){
            return requestMapping.method();
        }
        PostMapping postMapping= method.getAnnotation(PostMapping.class);
        if (postMapping!=null){
            return new RequestMethod[]{RequestMethod.POST};
        }
        GetMapping getMapping= method.getAnnotation(GetMapping.class);
        if (getMapping!=null){
            return new RequestMethod[]{RequestMethod.GET};
        }
        DeleteMapping deleteMapping= method.getAnnotation(DeleteMapping.class);
        if (deleteMapping!=null){
            return new RequestMethod[]{RequestMethod.DELETE};
        }
        PatchMapping patchMapping= method.getAnnotation(PatchMapping.class);
        if (patchMapping!=null){
            return new RequestMethod[]{RequestMethod.PATCH};
        }
        PutMapping putMapping= method.getAnnotation(PutMapping.class);
        if (putMapping!=null){
            return new RequestMethod[]{RequestMethod.PUT};
        }
        return null;
    }

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
    public Set<String> combinationUrl(String [] urlPrefix, String [] urlSuffix){
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
