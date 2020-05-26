package com.adongs.implement.captcha;


import com.adongs.annotation.core.Captcha;
import com.adongs.config.CaptchaGlobalConfig;
import com.adongs.constant.FailureCondition;
import com.adongs.exception.CaptchaException;
import com.adongs.implement.BaseAspect;
import com.adongs.implement.captcha.marker.MarkerFactory;
import com.adongs.implement.captcha.model.ResolveModel;
import com.adongs.implement.captcha.processor.CaptchaProcessor;
import com.adongs.implement.captcha.utils.AutomaticExtraction;
import com.adongs.session.terminal.Terminal;
import com.adongs.utils.el.ElAnalysis;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * 图片验证码
 */
@Aspect
public class CaptchaAspect extends BaseAspect {

    private static final AutomaticExtraction AUTOMATIC_EXTRACTION = new AutomaticExtraction();
    private static final Map<String, LinkedList<ResolveModel>> CODE_ROLE_MAP = Maps.newHashMap();
    private static final Map<String, LinkedList<ResolveModel>> ID_ROLE_MAP = Maps.newHashMap();
    @Autowired
    private CaptchaGlobalConfig config;
    @Autowired
    private CaptchaProcessor captchaProcessor;
    @Autowired
    private MarkerFactory markerFactory;

    private Map<String,String> header(){
        Map<String,String> header = Maps.newHashMap();
        final HttpServletRequest request = Terminal.getRequest();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            final String name = headerNames.nextElement();
            header.put(name,request.getHeader(name));
        }
        return header;
    }


    @Around(value = "@annotation(captcha)")
    public Object before(ProceedingJoinPoint joinPoint, Captcha captcha)throws Throwable{
        final Map<String, Object> params = getParams(joinPoint);
        final Object id = id(params, joinPoint.getSignature().toString(), captcha);
        final boolean threshold = markerFactory.threshold(id.toString(), Terminal.getRequest());
        if (threshold){
            Map<String, Object> codeParam = Maps.newHashMap(params);
            codeParam.putAll(header());
            final Object code = code(codeParam, joinPoint.getSignature().toString(), captcha);
            final boolean check = captchaProcessor.check(id.toString(), code.toString());
            if (!check){
                throw new CaptchaException("图片验证码错误");
            }else{
                markerFactory.remove(id.toString(), Terminal.getRequest());
            }
        }
        final HashSet<FailureCondition> failureConditions = Sets.newHashSet(condition(captcha));
        try {
            final Object proceed = joinPoint.proceed(joinPoint.getArgs());
            final boolean contains = failureConditions.contains(FailureCondition.EXPRESSION_DECISION);
            if (contains){
                params.put("return",proceed);
                FailureCondition.EXPRESSION_DECISION.mark(markerFactory,id.toString(),params,captcha.expression());
            }
            return proceed;
        }catch (Throwable e){
            final boolean contains = failureConditions.contains(FailureCondition.THROW_EXCEPTION);
            if (contains) {
                FailureCondition.THROW_EXCEPTION.mark(markerFactory, id.toString(), e, abnormalJudgment(captcha));
            }
            throw e;
        }
    }


    /**
     * 获取判定条件
     * @param captcha
     * @return 判定条件
     */
    private FailureCondition[] condition(Captcha captcha){
        final FailureCondition[] condition = captcha.condition();
        if (condition.length==0){
            return config.getCondition();
        }
        return condition;
    }

    /**
     * 获取异常判定类
     * @param captcha 注解
     * @return 判定类
     */
    private Class<? extends Throwable>[] abnormalJudgment(Captcha captcha){
        final Class<? extends Throwable>[] classes = captcha.abnormalJudgment();
        if (classes.length==0){
            return config.getAbnormalJudgment();
        }
        return classes;
    }

    /**
     * 获取id
     * @param params 参数
     * @param methodName 方法名
     * @param captcha 注解
     * @return id
     */
    private Object id(Map<String, Object> params,String methodName,Captcha captcha){
        final String loginName = captcha.loginName();
        if (StringUtils.isEmpty(loginName)){
            final LinkedList<ResolveModel> role = role(ID_ROLE_MAP,params, methodName, config.getLoginNameTry());
            if (role==null){
                throw new NullPointerException("id自动查找字段未找到字段");
            }
            return AUTOMATIC_EXTRACTION.resolve(params, role);
        }
        if (loginName.indexOf("#")==-1){
            final LinkedList<ResolveModel> role = role(ID_ROLE_MAP,params, methodName, loginName);
            if (role==null){
                throw new NullPointerException("id查找字段未找到字段 loginName:"+loginName);
            }
            return AUTOMATIC_EXTRACTION.resolve(params, role);
        }
        ElAnalysis elAnalysis = new ElAnalysis(params);
        return elAnalysis.analysis(loginName);

    }

    /**
     * 获取code值
     * @param captcha 注解
     * @param params 方法参数
     * @return code
     */
    private Object code(Map<String, Object> params,String methodName,Captcha captcha){
        final String code = captcha.code();
        if (StringUtils.isEmpty(code)){
            final LinkedList<ResolveModel> role = role(CODE_ROLE_MAP,params, methodName, config.getCodeTry());
            if (role==null){
                throw new NullPointerException("code自动查找字段未找到字段");
            }
            return AUTOMATIC_EXTRACTION.resolve(params, role);
        }
        if (code.indexOf("#")==-1){
            final LinkedList<ResolveModel> role = role(CODE_ROLE_MAP,params, methodName, code);
            if (role==null){
                throw new NullPointerException("code未找到指定字段 code:"+code);
            }
            return AUTOMATIC_EXTRACTION.resolve(params, role);
        }
        ElAnalysis elAnalysis = new ElAnalysis(params);
        return elAnalysis.analysis(code);
    }

    /**
     * 获取规则
     * @param role 规则
     * @param params 参数
     * @param methodName 方法名称
     * @param keyNames 获取的名称
     * @return 规则
     */
    private LinkedList<ResolveModel> role(Map<String, LinkedList<ResolveModel>> role,Map<String, Object> params,String methodName,String ... keyNames){
        final LinkedList<ResolveModel> resolveModels =role.get(methodName);
        if (resolveModels==null){
            for (String keyName : keyNames) {
                final LinkedList<ResolveModel> extract = AUTOMATIC_EXTRACTION.extract(params, keyName);
                if (extract!=null){
                    role.put(methodName,extract);
                    return extract;
                }
            }
        }
        return resolveModels;
    }

}
