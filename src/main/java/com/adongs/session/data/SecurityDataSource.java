package com.adongs.session.data;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public interface SecurityDataSource {


    /**
     * 获取token
     * @param token 用户token
     * @param <T> 泛型
     * @return 返回用户信息
     */
    public <T extends Serializable>Optional<T> token(Optional<String> token);


    /**
     * 获取角色
     * @param token 用户token
     * @return 获取角色
     */
    public Optional<Set<String>> roles(Optional<String> token);

    /**
     * 获取权限
     * @param token 用户token
     * @return 获取权限
     */
    public Optional<Set<String>> permissions(Optional<String> token);

    /**
     * 退出登录
     * @param token 用户token
     */
    public void logout(Optional<String> token);

}
