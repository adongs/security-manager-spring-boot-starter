package com.security.manager.data;


import com.security.manager.session.user.User;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DataSource {


    /**
     * 获取token
     * @param token
     * @return 返回用户信息
     */
    public Optional<User> token(Optional<String> token);


    /**
     * 获取角色
     * @param token
     * @return
     */
    public Set<String> roles(String token);

    /**
     * 获取权限
     * @param token
     * @return
     */
    public Set<String> permissions(String token);

    /**
     * 退出登录
     * @param token
     */
    public void logout(String token);

    /**
     * 更新token过期时间
     * @param token
     * @param time 更具过期时间
     * @return 返回过期时间 (单位毫秒)
     */
    public long updateTime(String token,long time);
}
