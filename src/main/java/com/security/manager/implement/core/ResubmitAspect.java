package com.security.manager.implement.core;

import com.security.manager.annotation.core.Resubmit;
import com.security.manager.exception.ResubmitException;
import com.security.manager.implement.BaseAspect;
import com.security.manager.implement.resubmit.ResubmitProcessor;
import com.security.manager.implement.sign.SignProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 防止重复提交
 */
@Aspect
@Order(2)
public class ResubmitAspect extends BaseAspect {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 重复提交
     * @param joinPoint
     * @param resubmit
     * @return
     * @throws Throwable
     */
    @Before(value = "@annotation(resubmit)")
    public void before(JoinPoint joinPoint, Resubmit resubmit)throws Throwable{
        ResubmitProcessor bean = applicationContext.getBean(resubmit.processor());
        Map<String, Object> params = getParams(joinPoint);
        String userId = resubmit.userId();
        if (!userId.equals("false")){
            userId = bean.userId(resubmit.userId(),params);
        }
        String key = bean.key(resubmit.uid(),params);
        String uidKey= userId+key;
        boolean presence = bean.isPresence(uidKey);
        if (presence){
            if (resubmit.refreshTime()){
                bean.update(uidKey,resubmit.time());
            }
            throw new ResubmitException("No duplicate submissions allowed");
        }else{
            bean.saveKey(uidKey,System.currentTimeMillis()+resubmit.time());
        }
    }
}
