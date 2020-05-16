package com.adongs.session.terminal;

import com.adongs.config.AuthenticationConfig;
import com.adongs.session.cache.CacheManager;
import com.adongs.session.data.SecurityDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


/**
 * 终端构建工厂
 * @author yudong
 * @version 1.0
 */
public class TerminalFactory {

    private final static Log LOGGER = LogFactory.getLog(TerminalFactory.class);

    private final AuthenticationConfig authenticationConfig;
    private final CacheManager cacheManager;
    private final SecurityDataSource dataSource;

    public TerminalFactory(AuthenticationConfig authenticationConfig, CacheManager cacheManager, SecurityDataSource dataSource) {
        this.authenticationConfig = authenticationConfig;
        this.cacheManager = cacheManager;
        this.dataSource = dataSource;
    }

    public Terminal create(ServletRequest servletRequest){
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final Optional<String> token = Optional.ofNullable(httpRequest.getHeader(authenticationConfig.getToken()));
        final Terminal terminal = new Terminal(token.isPresent()?new Token(token.get()):null,this);
        Terminal.set(terminal);
        return terminal;
    }

    protected void delete(AbstractTerminal terminal){
        dataSource.logout(Optional.ofNullable(terminal.token().getToken()));
        cacheManager.delete(terminal.token().getToken());
    }


}
