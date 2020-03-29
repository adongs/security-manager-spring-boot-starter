package com.adongs.implement.core;

import com.adongs.exception.ResubmitException;
import com.adongs.implement.resubmit.ResubmitProcessor;
import com.adongs.annotation.core.Resubmit;
import com.adongs.implement.BaseAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

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
     * @param joinPoint JoinPoint
     * @param resubmit Resubmit注解
     * @throws Throwable 异常
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
