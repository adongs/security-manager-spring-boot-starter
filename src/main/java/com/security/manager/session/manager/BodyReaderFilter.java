package com.security.manager.session.manager;

import com.security.manager.session.user.TerminalFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author yudong
 * @version 1.0
 * @date 2020/3/26 2:18 下午
 * @modified By
 */
public class BodyReaderFilter implements Filter  {

    private final static Log LOGGER = LogFactory.getLog(BodyReaderFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("BodyReaderFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(req);
        filterChain.doFilter(contentCachingRequestWrapper, servletResponse);
    }

    @Override
    public void destroy() {

    }

}