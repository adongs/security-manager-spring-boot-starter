package com.security.manager.session.user;

import com.security.manager.auto.SecurityManagerConfig;
import com.security.manager.data.DataSource;
import com.security.manager.exception.TokenException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author yudong
 * @version 1.0
 * @date 2020/2/4 2:55 下午
 * @modified By
 */
public class TerminalFactory {
    private final static Log logger = LogFactory.getLog(TerminalFactory.class);

    private SecurityManagerConfig config;
    private DataSource dataSource;

    public TerminalFactory(SecurityManagerConfig config, DataSource dataSource) {
        this.config = config;
        this.dataSource = dataSource;
    }

    public void terminal(HttpServletRequest httpServletRequest) {
        SecurityManagerConfig.Request request = config.getRequest();
        Optional<String> token = Optional.ofNullable(httpServletRequest.getHeader(request.getToken()));
        if (request.getTourists() && !token.isPresent()){
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
     * @param servletRequest
     * @return
     */
    public void terminal(ServletRequest servletRequest) {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        terminal(httpRequest);
    }


    /**
     * 构建终端信息
     * @param request
     * @param httpServletRequest
     * @param user
     * @param token
     * @return
     */
    private Terminal build(SecurityManagerConfig.Request request, HttpServletRequest httpServletRequest, User user, String token){
        String version = httpServletRequest.getHeader(config.getRequest().getVersion());
        String sign = httpServletRequest.getHeader(config.getRequest().getSign());
        String timestamp = httpServletRequest.getHeader(config.getRequest().getTimestamp());
        return new Terminal(user,version,sign,token,timestamp,this.dataSource);
    }
}
