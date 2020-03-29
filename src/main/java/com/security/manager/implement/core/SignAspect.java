package com.security.manager.implement.core;

import com.security.manager.annotation.core.Sign;
import com.security.manager.implement.BaseAspect;
import com.security.manager.implement.sign.SignProcessor;
import com.security.manager.session.user.Terminal;
import com.security.manager.utils.el.ElAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 授权注解解析
 * 认证当前token
 * 判断是否具有权限
 * 判断是否具有角色
 */
@Aspect
public class SignAspect extends BaseAspect {

    @Autowired
    private ApplicationContext applicationContext;

    @Before(value = "@annotation(sign)")
    public void before(JoinPoint joinPoint, Sign sign){
        SignProcessor bean = applicationContext.getBean(sign.value());
        Terminal terminal = Terminal.get();
        Map<String, Object> params = getParams(joinPoint);
        Object[] objects = params.values().toArray();
        String signStr = null;
        if (StringUtils.isEmpty(sign.sign())){
            signStr = terminal.getSign();
        }else{
            ElAnalysis elAnalysis = new ElAnalysis(params);
            signStr = elAnalysis.analysis(sign.sign(), String.class);
        }
        bean.check(signStr,objects);
    }





}
