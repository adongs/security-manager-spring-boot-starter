package com.security.manager.implement.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.manager.annotation.extend.IgnoresField;
import com.security.manager.annotation.extend.IgnoresFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;

/**
 * @Author yudong
 * @Date 2019/7/15 上午11:31
 * @Version 1.0
 */
@ControllerAdvice
public class DisparkResponseBody implements ResponseBodyAdvice{

    private static final Logger log = LoggerFactory.getLogger(DisparkResponseBody.class);


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        boolean hasDisparkIgnore= returnType.hasMethodAnnotation(IgnoresField.class);
        boolean hasDisparkIgnores = returnType.hasMethodAnnotation(IgnoresFields.class);
        return (hasDisparkIgnore || hasDisparkIgnores);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Annotation[] annos = returnType.getMethodAnnotations();
        CustomerJsonSerializer jsonSerializer = CustomerJsonSerializer.build(annos);
        Object toObject =  jsonSerializer.toObject(body);
        return toObject;
    }



}
