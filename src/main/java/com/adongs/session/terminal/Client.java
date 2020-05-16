package com.adongs.session.terminal;


import eu.bitwalker.useragentutils.UserAgent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 连接的客户端
 * @author yudong
 * @version 1.0
 */
public interface Client {

  /**
   * 获取终端访问类型
   * @return 终端类型
   */
  public UserAgent type();

  /**
   * 获取请求header
   * @return 请求header
   */
  public Map<String, List<String>> headers();

  /**
   * 获取token
   * @return token
   */
  public Token token();

  /**
   * 获取权限
   * @return 权限
   */
  public Set<String> authority();

  /**
   * 获取角色
   * @return 角色
   */
  public Set<String> role();

  /**
   * 设置值
   * @param key 键
   * @param value 值
   * @param <T> 泛型
   */
  public <T extends Serializable>void setAttribute(String key, T value);

  /**
   * 获取属性
   * @param key 键
   * @return 值
   */
  public Object getAttribute(String key);

}
