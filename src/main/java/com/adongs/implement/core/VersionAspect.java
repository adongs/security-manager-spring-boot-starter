package com.adongs.implement.core;


import com.adongs.annotation.core.Version;
import com.adongs.exception.VersionException;
import com.adongs.implement.BaseAspect;
import com.adongs.session.user.Terminal;
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
