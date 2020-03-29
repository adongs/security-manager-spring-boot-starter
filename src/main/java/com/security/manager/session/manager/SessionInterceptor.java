package com.security.manager.session.manager;

import com.security.manager.session.user.TerminalFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    private TerminalFactory sessionManagerFactory;

    public SessionInterceptor(TerminalFactory sessionManagerFactory) {
        this.sessionManagerFactory = sessionManagerFactory;
    }

    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        sessionManagerFactory.terminal(request);
       return true;
    }
}
