package com.adongs.session.filter;

import com.adongs.session.authenticate.AuthorityAuthentication;
import com.adongs.session.terminal.Terminal;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户认证
 * @author yudong
 * @version 1.0
 */
public class UserAuthenticationFilter extends AbstractSessionFilter{

    private final AuthorityAuthentication authentication;

    public UserAuthenticationFilter(DispatcherServlet dispatcherServlet, ApplicationContext context, AuthorityAuthentication authentication) {
        super(dispatcherServlet, context);
        this.authentication = authentication;
    }

    @Override
    public void execution(HttpServletRequest request, HttpServletResponse response) throws Exception {
        authentication.userAuthentication(request,Terminal.get());
    }
}
