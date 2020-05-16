package com.adongs.implement.core;


import com.adongs.annotation.core.Version;
import com.adongs.config.VersionConfig;
import com.adongs.exception.VersionException;
import com.adongs.implement.BaseAspect;
import com.adongs.session.terminal.Terminal;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Aspect
public class VersionAspect extends BaseAspect {

    @Autowired
    private VersionConfig config;

    @Before(value = "@annotation(version)")
    public void before(JoinPoint joinPoint, Version version){
        Set<String> versions = Sets.newHashSet(version.value());
        final String versionKey = config.getVersion();
        final HttpServletRequest request = Terminal.getRequest();
        String requestVersion = request.getHeader(versionKey);
        if (StringUtils.isEmpty(requestVersion)){
            requestVersion = request.getParameter(versionKey);
        }
        if (!versions.isEmpty() && StringUtils.isEmpty(requestVersion)){
            throw new VersionException("Version Numbers are not allowed");
        }
        if(!versions.contains(requestVersion)){
            throw new VersionException("Version Numbers are not allowed");
        }
    }




}
