package com.adongs.data;


import com.adongs.session.user.User;

import java.util.Optional;
import java.util.Set;

public interface DataSource {


    /**
     * 获取token
     * @param token 用户token
     * @return 返回用户信息
     */
    public Optional<User> token(Optional<String> token);


    /**
     * 获取角色
     * @param token 用户token
     * @return 获取角色
     */
    public Set<String> roles(String token);

    /**
     * 获取权限
     * @param token 用户token
     * @return 获取权限
     */
    public Set<String> permissions(String token);

    /**
     * 退出登录
     * @param token 用户token
     */
    public void logout(String token);

    /**
     * 更新token过期时间
     * @param token 用户token
     * @param time 更具过期时间
     * @return 返回过期时间 (单位毫秒)
     */
    public long updateTime(String token,long time);
}
