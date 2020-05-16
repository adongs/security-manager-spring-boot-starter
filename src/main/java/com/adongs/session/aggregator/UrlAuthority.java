package com.adongs.session.aggregator;

import com.adongs.constant.Logical;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * url权限
 * @author yudong
 * @version 1.0
 */
public class UrlAuthority {
    private final Set<String> ROLES = Sets.newHashSet();
    private final Logical ROLES_LOGICAL;
    private final Set<String> AUTHORITY = Sets.newHashSet();
    private final Logical AUTHORITY_LOGICAL;
    private final boolean IGNORE;

    protected UrlAuthority(Set<String> roles, Set<String> authority) {
        ROLES.addAll(roles);
        AUTHORITY.addAll(authority);
        ROLES_LOGICAL=Logical.OR;
        AUTHORITY_LOGICAL=Logical.OR;
        IGNORE = false;
    }

    protected UrlAuthority(Set<String> roles,Logical roleLogical,Set<String> authority,Logical authorityLogical) {
        ROLES.addAll(roles);
        AUTHORITY.addAll(authority);
        ROLES_LOGICAL=roleLogical;
        AUTHORITY_LOGICAL=authorityLogical;
        IGNORE = false;
    }

    protected UrlAuthority(boolean ignore) {
        this.IGNORE= ignore;
        ROLES_LOGICAL=null;
        AUTHORITY_LOGICAL=null;
    }

    /**
     * 汇总
     * @param urlAuthority 需要汇总的对象
     */
    protected void summary(UrlAuthority urlAuthority){
        ROLES.addAll(urlAuthority.getRoles());
        AUTHORITY.addAll(urlAuthority.getAuthority());

    }

    /**
     * 重新载入
     * @param roles 角色
     * @param authority 权限
     */
    protected void reload(Set<String> roles,Set<String> authority){
        ROLES.clear();
        ROLES.addAll(roles);
        AUTHORITY.clear();
        AUTHORITY.addAll(authority);
    }

    /**
     * 这个url是否为忽略url
     * @return true 忽略   false 不忽略
     */
    public boolean ignore(){
        return this.IGNORE;
    }


    public Set<String> getRoles() {
        return ImmutableSet.copyOf(ROLES);
    }

    public Set<String> getAuthority() {
        return ImmutableSet.copyOf(AUTHORITY);
    }

    public Logical getRolesLogical(){
        return ROLES_LOGICAL;
    }

    public Logical getAuthorityLogical(){
        return AUTHORITY_LOGICAL;
    }

    /**
     * 判断权限是否为空
     * @return true 权限为空  false 权限非空
     */
    public boolean isNull(){
       return ROLES.isEmpty() && AUTHORITY.isEmpty() && !this.IGNORE;
    }

}
