package com.adongs.session.user;


import com.adongs.data.DataSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;


public class Terminal {

    private final static ThreadLocal<Terminal> threadLocal = new ThreadLocal<Terminal>();
    private final User user;
    private final String version;
    private final String sign;
    private final String token;
    private final String timestamp;
    private final DataSource dataSource;


    protected <T extends User> Terminal(T user, String version, String sign, String token, String timestamp, DataSource dataSource) {
        this.user = user;
        this.version = version;
        this.sign = sign;
        this.token = token;
        this.timestamp = timestamp;
        this.dataSource = dataSource;
    }
    private Terminal(){
        this.user = null;
        this.version = null;
        this.sign = null;
        this.token = null;
        this.timestamp = null;
        this.dataSource = null;
    }

    /**
     * 退出登录
     */
    public void logout(){
        dataSource.logout(token);
        threadLocal.remove();
    }

    public <T extends User>T getUser(Class<T> clazz){
        if (user==null){
            return null;
        }
        return (T)user;
    }

    public User getUser(){
       return getUser(User.class);
    }

    public Set<String> getRoles(){
       return dataSource.roles(this.token);
    }

    public Set<String> getPermissions(){
        return dataSource.permissions(this.token);
    }

    /**
     * 获取当前请求
     * @param clazz 指定类型
     * @param <T> 指定类型
     * @return HttpServletRequest
     */
    public static <T extends ServletRequest>T getRequest(Class<T> clazz){
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes == null){
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
       return (T) request;
    }

    /**
     * 获取当前请求
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest(){
        return getRequest(HttpServletRequest.class);
    }

    /**
     * 获取请求版本
     * @return 版本号
     */
    public String getVersion(){
       return version;
    }

    /**
     * 获取盐值
     * @return 盐值
     */
    public String getSign(){
     return sign;
    }

    /**
     * 获取时间
     * @return 请求时间
     */
    public String getTimestamp(){
        return timestamp;
    }

    protected static void set(Terminal terminal){
        threadLocal.set(terminal);
    }

    public static Terminal get(){
        return threadLocal.get();
    }

}
