package com.adongs.session.user;

import com.adongs.auto.SecurityManagerConfig;
import com.adongs.data.DataSource;
import com.adongs.exception.TokenException;
import com.adongs.implement.sightseer.SightseerProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author yudong
 * @version 1.0
 */
public class TerminalFactory {
    private final static Log logger = LogFactory.getLog(TerminalFactory.class);

    private SecurityManagerConfig config;
    private DataSource dataSource;
    private SightseerProcessor sightseerProcessor;

    public TerminalFactory(SecurityManagerConfig config, DataSource dataSource,SightseerProcessor sightseerProcessor) {
        this.config = config;
        this.dataSource = dataSource;
        this.sightseerProcessor=sightseerProcessor;
    }

    public void terminal(HttpServletRequest httpServletRequest) {
        boolean sightseer = sightseerProcessor.isSightseer(httpServletRequest.getRequestURI());
        if (sightseer){return;}
        SecurityManagerConfig.Request request = config.getRequest();
        Optional<String> token = Optional.ofNullable(httpServletRequest.getHeader(request.getToken()));
        if (!request.getTourists() && !token.isPresent()){
            throw new TokenException("token invalid",null);
        }
        Optional<User> user = dataSource.token(token);
        if (!user.isPresent() && token.isPresent()){
            throw new TokenException("token inexistence",token.isPresent()?token.get():null);
        }
        Terminal build = build(request, httpServletRequest, user.isPresent()?user.get():null, token.isPresent()?token.get():null);
        build.set(build);
    }

    /**
     * 获取当前终端信息
     *
     * @param servletRequest ServletRequest
     */
    public void terminal(ServletRequest servletRequest) {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        terminal(httpRequest);
    }


    /**
     * 构建终端信息
     * @param request 请求配置
     * @param httpServletRequest HttpServletRequest
     * @param user 用户信息
     * @param token 用户token
     * @return 终端
     */
    private Terminal build(SecurityManagerConfig.Request request, HttpServletRequest httpServletRequest, User user, String token){
        String version = httpServletRequest.getHeader(config.getRequest().getVersion());
        String sign = httpServletRequest.getHeader(config.getRequest().getSign());
        String timestamp = httpServletRequest.getHeader(config.getRequest().getTimestamp());
        return new Terminal(user,version,sign,token,timestamp,this.dataSource);
    }
}
