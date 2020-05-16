package com.adongs.implement.captcha;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.MatchableHandlerMapping;
import org.springframework.web.servlet.handler.RequestMatchResult;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 二维码HandlerMapping
 * @author yudong
 * @version 1.0
 */
public class CaptchaEndpointHandlerMapping extends RequestMappingInfoHandlerMapping implements InitializingBean, MatchableHandlerMapping {

    private final CorsConfiguration corsConfiguration;

    private final CustomizeHandler customizeHandler;

    private final static RequestMappingInfo.BuilderConfiguration BUILDER_CONFIGURATION = getBuilderConfig();

    public CaptchaEndpointHandlerMapping(CorsConfiguration corsConfiguration,CustomizeHandler customizeHandler) {
        this.corsConfiguration = corsConfiguration;
        this.customizeHandler=customizeHandler;
        setOrder(-100);
    }

    @Override
    protected void initHandlerMethods() {
        PatternsRequestCondition patterns = patternsRequestConditionForPattern("/captcha");
        RequestMethodsRequestCondition methods = new RequestMethodsRequestCondition(RequestMethod.GET);
        ProducesRequestCondition produces = new ProducesRequestCondition("application/json");
        RequestMappingInfo mapping = new RequestMappingInfo(patterns, methods, null, null, null, produces, null);
        registerMapping(mapping, customizeHandler, ReflectionUtils.findMethod(customizeHandler.getClass(), "links",
                HttpServletRequest.class, HttpServletResponse.class));
    }

    @Override
    protected CorsConfiguration initCorsConfiguration(Object handler, Method method, RequestMappingInfo mapping) {
        return this.corsConfiguration;
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return false;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        return null;
    }

    @Override
    public RequestMatchResult match(HttpServletRequest request, String pattern) {
        RequestMappingInfo info = RequestMappingInfo.paths(pattern).options(BUILDER_CONFIGURATION).build();
        RequestMappingInfo matchingInfo = info.getMatchingCondition(request);
        if (matchingInfo == null) {
            return null;
        }
        Set<String> patterns = matchingInfo.getPatternsCondition().getPatterns();
        String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
        return new RequestMatchResult(patterns.iterator().next(), lookupPath, getPathMatcher());
    }



    private static RequestMappingInfo.BuilderConfiguration getBuilderConfig() {
        RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
        config.setUrlPathHelper(null);
        config.setPathMatcher(null);
        config.setSuffixPatternMatch(false);
        config.setTrailingSlashMatch(true);
        return config;
    }

    private PatternsRequestCondition patternsRequestConditionForPattern(String path) {
        String[] patterns = new String[] {path};
        return new PatternsRequestCondition(patterns, BUILDER_CONFIGURATION.getUrlPathHelper(), BUILDER_CONFIGURATION.getPathMatcher(),
                BUILDER_CONFIGURATION.useSuffixPatternMatch(), BUILDER_CONFIGURATION.useTrailingSlashMatch());
    }
}
