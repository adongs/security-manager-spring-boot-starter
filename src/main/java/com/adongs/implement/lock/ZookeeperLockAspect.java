package com.adongs.implement.lock;

import com.adongs.annotation.extend.lock.ZookeeperLock;
import com.adongs.implement.BaseAspect;
import com.adongs.implement.decrypt.coding.MD5;
import com.adongs.implement.lock.processor.LockProcessor;
import com.adongs.utils.el.ElAnalysis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * zookeeper锁
 */
@Aspect
public class ZookeeperLockAspect extends BaseAspect {
    @Autowired
    private ApplicationContext applicationContext;
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final MD5 md5 = MD5.build();

    @Around(value = "@annotation(lock)")
    public Object around(ProceedingJoinPoint joinPoint, ZookeeperLock lock)throws Throwable{
        LockProcessor lockProcessor = applicationContext.getBean(lock.processor());
        Object[] args = joinPoint.getArgs();
        Object proceed = null;
        String key = buildId(joinPoint,lock);
        Object locks = lockProcessor.lock(key, 0);
            try {
                proceed = joinPoint.proceed(args);
            } catch (Throwable throwable) {
                throw throwable;
            }finally {
                lockProcessor.unlock(locks);
            }

        return proceed;
    }


    private String buildId(ProceedingJoinPoint joinPoint, ZookeeperLock lock){
        Map<String, Object> params = getParams(joinPoint);
        if (StringUtils.isEmpty(lock.key())) {
            StringBuilder stringBuilder = new StringBuilder();
            String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
            stringBuilder.append(declaringTypeName);
            params.forEach((k, v) -> {
                String values = "";
                try {
                    values = OBJECT_MAPPER.writeValueAsString(v);
                } catch (JsonProcessingException e) {

                }
                stringBuilder.append(k).append("=").append(values);
            });
            return md5.encode(stringBuilder.toString());
        }else{
            ElAnalysis elAnalysis =new ElAnalysis(params);
            String analysis = elAnalysis.analysis(lock.key());
            return analysis;
        }
    }


}
