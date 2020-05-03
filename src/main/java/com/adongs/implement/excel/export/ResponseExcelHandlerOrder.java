package com.adongs.implement.excel.export;

import com.google.common.collect.Lists;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;
import java.util.Optional;

/**
 * spring boot 启动监听器
 * 由于在RequestMappingHandlerAdapter 中自定义HandlerMethodReturnValueHandler处理器靠后,
 * 在有@ResponseBody注解的情况下,我们自定义的注解存在无效的问题,
 * 这里重新设置处理器的顺序,将顺序提前
 * @author yudong
 * @version 1.0
 */
@Component
public class ResponseExcelHandlerOrder implements ApplicationListener<ApplicationStartedEvent> {

    /**
     * 将ResponseExcelHandler处理器提前
     * @param applicationReadyEvent 上下文已经准备完毕事件
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationReadyEvent) {
        ConfigurableApplicationContext applicationContext = applicationReadyEvent.getApplicationContext();
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        if (requestMappingHandlerAdapter!=null){
            List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
            List<HandlerMethodReturnValueHandler> newReturnValueHandlers = Lists.newArrayList(returnValueHandlers);
            Optional<HandlerMethodReturnValueHandler> any = newReturnValueHandlers.stream().filter(h -> h instanceof ResponseExcelHandler).findAny();
            if(any.isPresent()){
                newReturnValueHandlers.remove(any.get());
                newReturnValueHandlers.add(0,any.get());
            }
            requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
        }
    }
}
