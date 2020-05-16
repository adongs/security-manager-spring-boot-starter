package com.adongs.session.filter;

import com.adongs.session.terminal.TerminalFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 终端创建filter
 * 负责创建终端
 * @author yudong
 * @version 1.0
 */
public class TerminalFilter extends AbstractSessionFilter {

    private final static Log LOGGER = LogFactory.getLog(TerminalFilter.class);

    private final TerminalFactory terminalFactory;

    public TerminalFilter(TerminalFactory terminalFactory, DispatcherServlet dispatcherServlet, ApplicationContext context) {
        super(dispatcherServlet, context);
        this.terminalFactory=terminalFactory;
    }

    @Override
    public void execution(HttpServletRequest request, HttpServletResponse response) throws Exception {
        terminalFactory.create(request);
    }
}
