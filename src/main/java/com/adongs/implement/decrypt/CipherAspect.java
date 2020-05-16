package com.adongs.implement.decrypt;

import com.adongs.annotation.extend.encryption.Cipher;
import com.adongs.annotation.extend.encryption.Decode;
import com.adongs.annotation.extend.encryption.Decrypt;
import com.adongs.annotation.extend.encryption.ReturnCipher;
import com.adongs.config.CipherConfig;
import com.adongs.constant.ReturnCipherType;
import com.adongs.implement.AbstractContextAspect;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.compress.utils.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;


/**
 * 密码(加密和解密的统称)处理
 */
@Aspect
@Component
public class CipherAspect extends AbstractContextAspect<DecryptProcessor> {

    /**
     * 缓存类的字段信息,加快解析速度
     */
    private static final Map<String, Set<FieldExpand>>  FIELD_MAP = Maps.newHashMap();
    /**
     * 缓存方法信息,加快解析速度
     */
    private static final Map<String,MethodExpand> METHOD_MAP = Maps.newHashMap();

    @Autowired
    private CipherConfig config;

    public CipherAspect(ApplicationContext applicationContext) {
        super(applicationContext, DecryptProcessor.class);
    }

    /**
     * 方法上有@ReturnCipher注解的
     */
    @Pointcut("@annotation(com.adongs.annotation.extend.encryption.ReturnCipher)")
    public void returnCipher(){}
    /**
     * 方法参数上有@Cipher注解的
     */
    @Pointcut("execution(public * *(.., @com.adongs.annotation.extend.encryption.Cipher (*) ,..))")
    public void cipher(){}

    /**
     * 解密 方法参数上有@Decode
     */
    @Pointcut("execution(public * *(.., @com.adongs.annotation.extend.encryption.Decode (*) ,..))")
    public void decode(){}

    /**
     * 加密 方法参数上有@Decrypt
     */
    @Pointcut("execution(public * *(.., @com.adongs.annotation.extend.encryption.Decrypt (*) ,..))")
    public void decrypt(){}


    /**
     * 提取类的加密或者解密字段
     * 只能处理自定义类型,如果不是自定义类型将返回空
     * @param clazz 处理类型
     * @return 字段信息集合
     */
    private Set<FieldExpand> fieldExpands(final Class<?> clazz){
        if (clazz==null){
            return null;
        }
        boolean isObjectCustom = clazz.getClassLoader()==null;
        if (isObjectCustom){
            return null;
        }
        final String typeName = clazz.getTypeName();
        Set<FieldExpand> fieldExpands = FIELD_MAP.get(typeName);
        if (fieldExpands!=null){
            return fieldExpands;
        }
        fieldExpands = Sets.newHashSet();
        final Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            final Cipher cipher = field.getAnnotation(Cipher.class);
            if (cipher!=null){
                boolean isCustom = field.getType().getClassLoader()!=null;
                if (isCustom){
                    final FieldExpand.Ciphere ciphere = new FieldExpand.Ciphere(field.getType(),cipher);
                    final FieldExpand fieldExpand = new FieldExpand(field, ciphere);
                    field.setAccessible(true);
                    fieldExpands.add(fieldExpand);
                    continue;
                }
            }
            final Decode decode = field.getAnnotation(Decode.class);
            boolean isString = field.getType().isAssignableFrom(String.class);
            if (decode!=null && isString){
                    final FieldExpand.Decodee decodee = new FieldExpand.Decodee(field.getType(),decode);
                    final FieldExpand fieldExpand = new FieldExpand(field, decodee);
                    field.setAccessible(true);
                    fieldExpands.add(fieldExpand);
                    continue;
            }
            final Decrypt decrypt = field.getAnnotation(Decrypt.class);
            if (decrypt!=null && isString){
                    final FieldExpand.Decrypte decrypte = new FieldExpand.Decrypte(field.getType(),decrypt);
                    final FieldExpand fieldExpand = new FieldExpand(field, decrypte);
                    field.setAccessible(true);
                    fieldExpands.add(fieldExpand);
                    continue;
            }
        }
        if (!fieldExpands.isEmpty()){
            FIELD_MAP.put(typeName,fieldExpands);
            return fieldExpands;
        }
        return null;
    }

    /**
     * 提取方法的注解
     * @param joinPoint 注解信息
     * @return 方法注解信息
     */
    private MethodExpand methodExpand(final ProceedingJoinPoint joinPoint)throws NoSuchMethodException{
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final String methodInfo= methodSignature.toString();
         MethodExpand methodExpand = METHOD_MAP.get(methodInfo);
        if (methodExpand!=null){
            return methodExpand;
        }
        Class<?>[] parameterTypes = methodSignature.getMethod().getParameterTypes();
        String[] parameterNames = methodSignature.getParameterNames();
        final Annotation[][] parameterAnnotations = joinPoint.getTarget().getClass().
                getMethod(methodSignature.getMethod().getName(), parameterTypes).getParameterAnnotations();
        List<MethodExpand.Param> params = Lists.newArrayList();
        for (int i = 0,l=parameterAnnotations.length; i < l; i++) {
            final HashSet<Annotation> annotations = Sets.newHashSet(parameterAnnotations[i]);
            final String parameterName = parameterNames[i];
            final Class<?> parameterType = parameterTypes[i];
            final Optional<Annotation> cipher = annotations.stream().filter(a -> a.annotationType() == Cipher.class).findAny();
            boolean isCustom = parameterType.getClassLoader()!=null;
            boolean isCollection = parameterType.getClass().isAssignableFrom(Collection.class.getClass());
            if (cipher.isPresent() && (isCustom || isCollection)){
                MethodExpand.Param param = new MethodExpand.Cipherp(parameterName,parameterType,(Cipher)cipher.get());
                params.add(param);
                continue;
            }
            final Optional<Annotation>  decode = annotations.stream().filter(a -> a.annotationType() == Decode.class).findAny();
            final boolean isString = String.class.getClass().isInstance(parameterType);
            if (decode.isPresent() && isString){
                MethodExpand.Param param = new MethodExpand.Decodep(parameterName,parameterType,(Decode)decode.get());
                params.add(param);
                continue;
            }
            final Optional<Annotation>  decrypt = annotations.stream().filter(a -> a.annotationType() == Decrypt.class).findAny();
            if (decrypt.isPresent() && isString){
                MethodExpand.Param param = new MethodExpand.Decryptp(parameterName,parameterType,(Decrypt)decrypt.get());
                params.add(param);
                continue;
            }
        }
        final Class returnType = methodSignature.getReturnType();
        final ReturnCipher annotation = methodSignature.getMethod().getAnnotation(ReturnCipher.class);
        MethodExpand.Return aReturn = null;
        if (annotation!=null) {
             aReturn = new MethodExpand.Return(returnType, annotation);
        }
        methodExpand = new MethodExpand(params,aReturn);
        METHOD_MAP.put(methodInfo,methodExpand);
        return methodExpand;
    }


    /**
     * 处理参数对参数进行加密或者解密
     * @param joinPoint 注解信息
     */
    private Object [] paramsProcessing(ProceedingJoinPoint joinPoint) throws Throwable {
        final Map<String, Object> params = getParams(joinPoint);
        final MethodExpand methodExpand = methodExpand(joinPoint);
        final List<MethodExpand.Param> paramsExpand = methodExpand.getParams();
        for (MethodExpand.Param param : paramsExpand) {
            final Object o = params.get(param.getName());
            if (param instanceof MethodExpand.Cipherp){
                    paramModel(o);
                    continue;
            }
            if (param instanceof MethodExpand.Decodep){
                if (o instanceof String){
                    MethodExpand.Decodep decodep = (MethodExpand.Decodep)param;
                    final String string = paramString(o.toString(), decodep.getDecode());
                    params.replace(param.getName(),string);
                    continue;
                }
            }
            if (param instanceof MethodExpand.Decryptp){
                if (o instanceof String){
                    MethodExpand.Decryptp decryptp = (MethodExpand.Decryptp)param;
                    final String string = paramString(o.toString(), decryptp.getDecrypt());
                    params.replace(param.getName(),string);
                    continue;
                }
            }
        }
        return params.values().toArray();
    }

    /**
     * 参数模型中的加密或者解密支持list<model>
     * @param data 数据对象
     */
    private void paramModel(Object data) throws IllegalAccessException {
        if (data==null){
            return;
        }
        boolean isCollection = data instanceof Collection;
        if (isCollection){
            Collection collection = (Collection)data;
            for (Object object : collection) {
                 if (object!=null) {
                     paramModel(object);
                 }
            }
            return;
        }
        final Set<FieldExpand> fieldExpands = fieldExpands(data.getClass());
        if (fieldExpands==null){
            return;
        }
        for (FieldExpand fieldExpand : fieldExpands) {
            final Field field = fieldExpand.getField();
            final FieldExpand.Expand expand = fieldExpand.getExpand();
            if (expand instanceof FieldExpand.Ciphere){
                paramModel(field.get(data));
            }
            if(expand instanceof FieldExpand.Decodee){
                final DecryptProcessor processor = processor(expand.processor(), config.getProcessor());
                final String decodeData = processor.decode(expand.type(), field.get(data).toString());
                field.set(data,decodeData);
            }
            if(expand instanceof FieldExpand.Decrypte){
                final DecryptProcessor processor = processor(expand.processor(), config.getProcessor());
                final String decodeData = processor.encryption(expand.type(), field.get(data).toString());
                field.set(data,decodeData);
            }
        }
    }

    /**
     * 参数String的加密或者解密
     * @param data 数据对象
     */
    private String paramString(String data,Object object){
        if (object instanceof Decode) {
            Decode decode = (Decode)object;
            final DecryptProcessor processor = processor(decode.processor(), config.getProcessor());
            return processor.decode(decode.value(), data);
        }
        if (object instanceof Decrypt) {
            Decrypt decrypt = (Decrypt)object;
            final DecryptProcessor processor = processor(decrypt.processor(), config.getProcessor());
            return processor.encryption(decrypt.value(), data);
        }
        return null;
    }






    /**
     * 执行者
     * @param joinPoint 注解信息
     * @return 处理后的数据
     * @throws Throwable Throwable
     */
    @Around("returnCipher() || cipher() || decode() || decrypt()")
    public Object around(ProceedingJoinPoint joinPoint)throws Throwable {
        final Object[] objects = paramsProcessing(joinPoint);
        final Object proceed = joinPoint.proceed(objects);
        final MethodExpand methodExpand = methodExpand(joinPoint);
        final MethodExpand.Return aReturn = methodExpand.getaReturn();
        if (aReturn!=null){
           return returnProcessing(proceed,aReturn);
        }
      return proceed;
    }


    /**
     * 处理返回值
     * @param proceed 原始返回值
     * @param aReturn 处理注解信息
     * @return 处理后的返回值
     * @throws IllegalAccessException IllegalAccessException
     */
    private Object returnProcessing(Object proceed,MethodExpand.Return aReturn) throws IllegalAccessException {
        if (proceed==null){
            return null;
        }
        if (proceed instanceof Collection){
            Collection collection = (Collection)proceed;
            for (Object o : collection) {
                returnProcessing(o,aReturn);
            }
        }
        final ReturnCipher returnCipher = aReturn.getReturnCipher();
        final boolean isCustom = proceed.getClass().getClassLoader() != null;
        if (isCustom && returnCipher.value()==ReturnCipherType.MODEL){
            paramModel(proceed);
        }
        if (proceed instanceof String){
            if (returnCipher.value()==ReturnCipherType.DECODE){
               return paramString(proceed.toString(),returnCipher.decode());
            }
            if (returnCipher.value()==ReturnCipherType.DECRYPT){
                return paramString(proceed.toString(),returnCipher.decrypt());
            }
        }
        return proceed;
    }


}
