package com.adongs.session.terminal;

import com.adongs.exception.AuthorityException;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.compress.utils.Lists;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 抽象出终端对象
 * @author yudong
 * @version 1.0
 */
public abstract class AbstractTerminal<T extends Serializable> implements Client{
    private  Set<String> authority;
    private  Set<String> roles;
    private  Map<String,Object> attribute = Maps.newHashMap();
    private  UserAgent userAgent;
    private final Token token;
    private final TerminalFactory terminalFactory;
    private  T user;

    protected  AbstractTerminal(Token token,TerminalFactory terminalFactory) {
        this.token = token;
        this.terminalFactory=terminalFactory;
    }


    /**
     * 获取用户
     * @return 用户
     */
    public Object getUser(){
        return user;
    }

    public void addUser(T user){
        if (this.user==null){
            this.user=user;
        }else{
            throw new AuthorityException("Do not allow repeated additions");
        }
    }

    /**
     * 登出
     */
    public void logout(){
        terminalFactory.delete(this);
    };

    @Override
    public UserAgent type() {
        if (userAgent!=null){
            return userAgent;
        }
        HttpServletRequest request = getRequest();
        this.userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return this.userAgent;
    }

    @Override
    public Map<String, List<String>> headers() {
        Map<String, List<String>> headers = Maps.newHashMap();
        HttpServletRequest request = getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            List<String> header = header(name);
            headers.put(name,header);
        }
        return headers;
    }

    public List<String> header(String name) {
        List<String> header = Lists.newArrayList();
        HttpServletRequest request = getRequest();
        Enumeration<String> headers = request.getHeaders(name);
        if (headers!=null){
            while (headers.hasMoreElements()){
                header.add(headers.nextElement());
            }
        }
        return header;
    }

    @Override
    public Token token() {
        return token;
    }

    @Override
    public Set<String> authority() {
        return ImmutableSet.copyOf(this.authority);
    }

    @Override
    public Set<String> role() {
        return ImmutableSet.copyOf(this.roles);
    }

    public void addAuthority(Set<String> authority){
        if (this.authority==null){
            this.authority = Sets.newHashSet(authority);
        }else{
            throw new AuthorityException("Do not allow repeated additions");
        }
    }

    public void addRoles(Set<String> roles){
        if (this.roles==null){
            this.roles = Sets.newHashSet(roles);
        }else{
            throw new AuthorityException("Do not allow repeated additions");
        }
    }

    @Override
    public <T extends Serializable> void setAttribute(@NonNull String key, T value) {
        if (value==null){
            attribute.remove(key);
        }else{
            attribute.put(key,value);
        }

    }

    /**
     * 获取属性
     * @param key 键
     * @return 值
     */
    @Override
    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    /**
     * 获取属性
     * @param key 键
     * @param clazz 指定类型
     * @param <T> 泛型要求实现Serializable接口
     * @return 值
     */
    public <T extends Serializable> T getAttribute(String key, Class<T> clazz) {
        Object attribute = getAttribute(key);
        return (T)attribute;
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
}
