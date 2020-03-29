package com.adongs.implement.field;

import com.adongs.annotation.extend.IgnoresField;
import com.adongs.annotation.extend.IgnoresFields;
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
 * @author yudong
 * @version  1.0
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
