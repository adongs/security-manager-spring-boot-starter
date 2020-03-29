package com.security.manager.implement.core;


import com.security.manager.annotation.core.Version;
import com.security.manager.exception.VersionException;
import com.security.manager.implement.BaseAspect;
import com.security.manager.session.user.Terminal;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class VersionAspect extends BaseAspect {

    @Before(value = "@annotation(version)")
    public void before(JoinPoint joinPoint, Version version){
        Terminal terminal = Terminal.get();
        final String versionStr = terminal.getVersion();
        if (versionStr==null){
            throw new VersionException("Version Numbers are not allowed");
        }
        String[] versions = version.value();
        for (String s : versions) {
            if (versionStr.equals(s)){
                return;
            }
        }
        throw new VersionException("Version Numbers are not allowed");
    }

}
