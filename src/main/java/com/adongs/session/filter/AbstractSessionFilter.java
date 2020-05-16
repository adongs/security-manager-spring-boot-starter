package com.adongs.session.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * 抽象公共的filter
 * @author yudong
 * @version 1.0
 */
public abstract class AbstractSessionFilter implements Filter {
    private final static Log LOGGER = LogFactory.getLog(AbstractSessionFilter.class);
    private final DispatcherServlet dispatcherServlet;
    private final ApplicationContext context;


    public AbstractSessionFilter(DispatcherServlet dispatcherServlet, ApplicationContext context) {
        this.dispatcherServlet = dispatcherServlet;
        this.context = context;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * 负责代理执行 异常后将异常抛给spring mvc异常处理机制
     * @param request 请求
     * @param response 响应
     * @param chain filter链
     * @throws IOException io异常
     * @throws ServletException 异常
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse =  (HttpServletResponse) response;
          try{
               execution(httpServletRequest,httpServletResponse);
           }catch (Exception e){
              try {
                  final boolean exception = throwsException(httpServletRequest, httpServletResponse, e);
                  if (exception){
                      return;
                  }
                  throw e;
              }catch (Exception exception){
                  LOGGER.error(exception.getMessage(),exception);
                 throw new ServletException(exception.getMessage(),exception);
              }
          }
        chain.doFilter(request, response);
    }

    /**
     * 真正的执行者
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public abstract void execution(HttpServletRequest request, HttpServletResponse response)throws Exception;

    /**
     * 抛出异常到spring mvc全局处理
     * @param request 请求对象
     * @param response 响应对象
     * @param exception 异常
     * @throws Exception 异常
     * @return  true 处理成功  false未找到处理的方法
     */
    private boolean throwsException(HttpServletRequest request, HttpServletResponse response, Exception exception)throws Exception{
        for(Iterator<HandlerMapping> iterator = dispatcherServlet.getHandlerMappings().iterator(); iterator.hasNext();){
            HandlerMapping handlerMapping = iterator.next();
            HandlerExecutionChain handlerExecutionChain = handlerMapping.getHandler(request);
            if (handlerExecutionChain!=null){
                Object handler = handlerExecutionChain.getHandler();
                Map<String, HandlerExceptionResolver> matchingBeans = BeanFactoryUtils
                        .beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class, true, false);
                for(Iterator<HandlerExceptionResolver> resolver = matchingBeans.values().iterator();resolver.hasNext();){
                    ModelAndView modelAndView = resolver.next().resolveException(request, response, handler, exception);
                    if (modelAndView!=null){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void destroy() {}
}
