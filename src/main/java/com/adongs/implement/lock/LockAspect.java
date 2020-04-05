package com.adongs.implement.lock;

import com.adongs.annotation.extend.lock.Lock;
import com.adongs.implement.BaseAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 本地锁
 */
@Aspect
public class LockAspect extends BaseAspect {


    @Before(value = "@annotation(lock)")
    public void before(JoinPoint joinPoint, Lock lock){

    }





}
