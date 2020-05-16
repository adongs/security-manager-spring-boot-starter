package com.adongs.session.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * HttpServletRequest 重复读取
 * @author yudong
 * @version 1.0
 */
public class BodyRepeatedReadingFilter implements Filter  {

    private final static Log LOGGER = LogFactory.getLog(BodyRepeatedReadingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("init BodyReaderFilter");
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