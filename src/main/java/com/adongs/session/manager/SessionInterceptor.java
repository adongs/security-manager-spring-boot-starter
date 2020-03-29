package com.adongs.session.manager;

import com.adongs.session.user.TerminalFactory;
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
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler Object 代理方法对象
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        sessionManagerFactory.terminal(request);
       return true;
    }
}
