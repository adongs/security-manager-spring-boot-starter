package com.adongs.implement.core;

import com.adongs.annotation.core.Sign;
import com.adongs.config.SignConfig;
import com.adongs.exception.SignException;
import com.adongs.implement.AbstractContextAspect;
import com.adongs.implement.BaseAspect;
import com.adongs.implement.sign.SignProcessor;
import com.adongs.session.terminal.Terminal;
import com.adongs.utils.el.ElAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;


/**
 * 授权注解解析
 * 认证当前token
 * 判断是否具有权限
 * 判断是否具有角色
 */
@Aspect
public class SignAspect extends AbstractContextAspect<SignProcessor> {

   @Autowired
    private SignConfig config;

    public SignAspect(ApplicationContext applicationContext) {
        super(applicationContext, SignProcessor.class);
    }

    @Before(value = "@annotation(sign)")
    public void before(JoinPoint joinPoint, Sign sign){
        final SignProcessor processor = processor(sign.processor(), config.getProcessor());
        Map<String, Object> params = getParams(joinPoint);
        final String signStr = processor.sign(sign.sign(), params);
        final String calculate = processor.calculate(params);
        if (!signStr.equals(calculate)){
            throw new SignException("盐值校验失败");
        }
    }
}
