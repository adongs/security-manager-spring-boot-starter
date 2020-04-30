package com.adongs.session.manager;

import com.adongs.session.user.TerminalFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SessionFilter implements Filter {

    private final static Log LOGGER = LogFactory.getLog(SessionFilter.class);

    private TerminalFactory sessionManagerFactory;
    private DispatcherServlet dispatcherServlet;
    private ApplicationContext context;

    public SessionFilter(TerminalFactory sessionManagerFactory,DispatcherServlet dispatcherServlet,ApplicationContext context) {
        this.sessionManagerFactory = sessionManagerFactory;
        this.dispatcherServlet=dispatcherServlet;
        this.context=context;
    }

    /**
     * Called by the web container to indicate to a filter that it is
     * being placed into service.
     *
     * <p>The servlet container calls the init
     * method exactly once after instantiating the filter. The init
     * method must complete successfully before the filter is asked to do any
     * filtering work.
     *
     * <p>The web container cannot place the filter into service if the init
     * method either
     * <ol>
     * <li>Throws a ServletException
     * <li>Does not return within a time period defined by the web container
     * </ol>
     *
     * @param filterConfig FilterConfig
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("init SessionFilter");
    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the
     * container each time a request/response pair is passed through the
     * chain due to a client request for a resource at the end of the chain.
     * The FilterChain passed in to this method allows the Filter to pass
     * on the request and response to the next entity in the chain.
     *
     * <p>A typical implementation of this method would follow the following
     * pattern:
     * <ol>
     * <li>Examine the request
     * <li>Optionally wrap the request object with a custom implementation to
     * filter content or headers for input filtering
     * <li>Optionally wrap the response object with a custom implementation to
     * filter content or headers for output filtering
     * <li>
     * <ul>
     * <li><strong>Either</strong> invoke the next entity in the chain
     * using the FilterChain object
     * (<code>chain.doFilter()</code>),
     * <li><strong>or</strong> not pass on the request/response pair to
     * the next entity in the filter chain to
     * block the request processing
     * </ul>
     * <li>Directly set headers on the response after invocation of the
     * next entity in the filter chain.
     * </ol>
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @param chain FilterChain
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            sessionManagerFactory.terminal(request);
        }catch (Exception e){
            try {
                throwsException((HttpServletRequest)request,(HttpServletResponse)response,e);
                return;
            } catch (Exception exception) {
               LOGGER.error(exception.getMessage(),exception);
            }
        }
        chain.doFilter(request,response);
    }

    /**
     * 抛出异常到spring mvc全局处理
     * @param request 请求对象
     * @param response 响应对象
     * @param exception 异常
     * @throws Exception 异常
     */
    private void throwsException(HttpServletRequest request, HttpServletResponse response, Exception exception)throws Exception{
        for(Iterator<HandlerMapping> iterator = dispatcherServlet.getHandlerMappings().iterator();iterator.hasNext();){
            HandlerMapping handlerMapping = iterator.next();
            HandlerExecutionChain handlerExecutionChain = handlerMapping.getHandler(request);
            if (handlerExecutionChain!=null){
                Object handler = null;//handlerExecutionChain.getHandler();
                Map<String, HandlerExceptionResolver> matchingBeans = BeanFactoryUtils
                        .beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class, true, false);
                for(Iterator<HandlerExceptionResolver> resolver = matchingBeans.values().iterator();resolver.hasNext();){
                    ModelAndView modelAndView = resolver.next().resolveException(request, response, handler, exception);
                    if (modelAndView!=null){
                        return;
                    }
                }
            }
        }
    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service.
     *
     * <p>This method is only called once all threads within the filter's
     * doFilter method have exited or after a timeout period has passed.
     * After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter.
     *
     * <p>This method gives the filter an opportunity to clean up any
     * resources that are being held (for example, memory, file handles,
     * threads) and make sure that any persistent state is synchronized
     * with the filter's current state in memory.
     */
    public void destroy() {

    }
}
