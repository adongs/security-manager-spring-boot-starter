package com.security.manager.implement.logout;

import com.security.manager.annotation.extend.log.*;
import com.security.manager.constant.LogLevel;
import com.security.manager.implement.BaseAspect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogAspect extends BaseAspect {


    @Autowired
    private ApplicationContext applicationContext;

    @Around(value = "@annotation(logout)")
    public Object aroundLogout(ProceedingJoinPoint proceedingJoinPoint, Logout logout)throws Throwable {
        return logout(proceedingJoinPoint,logout.format(),logout.label(),logout.content(),logout.level());
    }

    @Around(value = "@annotation(logout)")
    public Object aroundLogDebug(ProceedingJoinPoint proceedingJoinPoint, LogDebug logout)throws Throwable {
        return logout(proceedingJoinPoint,logout.format(),logout.label(),logout.content(),LogLevel.DEBUG);
    }

    @Around(value = "@annotation(logout)")
    public Object aroundLogError(ProceedingJoinPoint proceedingJoinPoint, LogError logout)throws Throwable {
        return logout(proceedingJoinPoint,logout.format(),logout.label(),logout.content(),LogLevel.ERROR);
    }

    @Around(value = "@annotation(logout)")
    public Object aroundLogFatal(ProceedingJoinPoint proceedingJoinPoint, LogFatal logout)throws Throwable {
        return logout(proceedingJoinPoint,logout.format(),logout.label(),logout.content(),LogLevel.FATAL);
    }

    @Around(value = "@annotation(logout)")
    public Object aroundLogInfo(ProceedingJoinPoint proceedingJoinPoint, LogInfo logout)throws Throwable {
        return logout(proceedingJoinPoint,logout.format(),logout.label(),logout.content(),LogLevel.INFO);
    }

    @Around(value = "@annotation(logout)")
    public Object aroundLogTrace(ProceedingJoinPoint proceedingJoinPoint, LogTrace logout)throws Throwable {
        return logout(proceedingJoinPoint,logout.format(),logout.label(),logout.content(),LogLevel.TRACE);
    }

    @Around(value = "@annotation(logout)")
    public Object aroundLogWarn(ProceedingJoinPoint proceedingJoinPoint, LogWarn logout)throws Throwable {
        return logout(proceedingJoinPoint,logout.format(),logout.label(),logout.content(),LogLevel.WARN);
    }

    /**
     * 执行方法
     * @param proceedingJoinPoint
     * @param format
     * @param label
     * @param content
     * @param levels
     * @return
     * @throws Throwable
     */
    private Object logout(ProceedingJoinPoint proceedingJoinPoint,Class<? extends LogFormat> format,String label,String content,LogLevel ... levels)throws Throwable{
        Log log = LogFactory.getLog(proceedingJoinPoint.getSignature().getDeclaringType());
        boolean isOut = isOut(log,levels);
        if (isOut){
            LogFormat bean = applicationContext.getBean(format);
            Object[] args = proceedingJoinPoint.getArgs();
            Object proceed = null;
            Throwable throwable = null;
            StringBuffer contentOut;
            try {
                proceed = proceedingJoinPoint.proceed(args);
            } catch (Throwable e) {
                throwable = e;
            }
            LogFormat.LogOutInfo logOutInfo = new LogFormat.LogOutInfo(proceedingJoinPoint, label, getParams(proceedingJoinPoint), proceed, throwable);

            if (content.length()>0){
                contentOut = bean.customize(logOutInfo, content);
            }else{
                contentOut = bean.format(logOutInfo);
            }
            out(contentOut,log,levels);
            if (throwable!=null){
                throw throwable;
            }
            return proceed;
        }
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }


    /**
     * 判断是否输出
     * @param logger
     * @param logLevels
     * @return
     */
    private boolean isOut(Log logger,LogLevel ... logLevels){
        if (logLevels.length==0){return false;}
        for (int i = 0,l=logLevels.length; i <l; i++) {
            boolean out = logLevels[i].isOut(logger);
            if (out){
                return true;
            }
        }
        return false;
    }

    /**
     * 输出日志
     * @param stringBuffer
     * @param logLevels
     */
    private void out(StringBuffer stringBuffer,Log logger,LogLevel ... logLevels){
        if (stringBuffer==null || stringBuffer.length()==0){return;}
        for (int i = 0,l=logLevels.length; i <l ; i++) {
            logLevels[i].out(logger,stringBuffer.toString());
        }
    }


}
