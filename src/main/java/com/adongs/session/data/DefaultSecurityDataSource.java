package com.adongs.session.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 使用样例
 * @author yudong
 * @version 1.0
 */
public class DefaultSecurityDataSource implements SecurityDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSecurityDataSource.class);
    private Map<String,Users> userMap = Maps.newHashMap();


    @PostConstruct
    public void init(){
        String token = RandomStringUtils.randomAlphabetic(10);
        LOGGER.info("init token {}",token);
        String name = "adongs";
        Integer id = 1;
        LOGGER.info("init user info name:{}  id:{}",name,id);
        Set<String> roles = Sets.newHashSet("user","admin");
        LOGGER.info("init roles {}",roles);
        Set<String> permissions = Sets.newHashSet("delete","update","select","install");
        LOGGER.info("init permissions {}",permissions);
        Users user = new Users(name,id,roles,permissions,-1l);
        userMap.put(token,user);
    }

    private Optional<Users> getUser(Optional<String> token){
        Optional<Users> user = token.isPresent()?Optional.ofNullable(userMap.get(token.get())):Optional.empty();
        if (user.isPresent()){
            if (user.get().getTimeOut()!=-1 && user.get().getTimeOut()<System.currentTimeMillis()){
                return Optional.empty();
            }
        }
        return user;
    }

    @Override
    public <T extends Serializable> Optional<T> token(Optional<String> token) {
        Optional<Users> user = getUser(token);
        return (Optional<T>) user;
    }

    @Override
    public Optional<Set<String>> roles(Optional<String> token) {
        Optional<Users> user = getUser(token);
        if (user.isPresent()){
            return Optional.ofNullable(user.get().getRoles());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Set<String>> permissions(Optional<String> token) {
        Optional<Users> user = getUser(token);
        if (user.isPresent()){
            return Optional.ofNullable(user.get().getPermissions());
        }
        return Optional.empty();
    }

    @Override
    public void logout(Optional<String> token) {
        if (token.isPresent()){
            userMap.remove(token.get());
        }
    }


    /**
     * 用户对象
     */
    public static class Users implements Serializable{
        private static final long serialVersionUID = -5925170546792494230L;
        private String name;
        private Integer id;
        private Set<String> roles;
        private Set<String> permissions;
        private Long timeOut = null;


        public Users(String name, Integer id, Set<String> roles, Set<String> permissions, Long timeOut) {
            this.name = name;
            this.id=id;
            this.roles = roles;
            this.permissions = permissions;
            this.timeOut = timeOut;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public Set<String> getRoles() {
            return roles;
        }

        public void setRoles(Set<String> roles) {
            this.roles = roles;
        }

        public Set<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(Set<String> permissions) {
            this.permissions = permissions;
        }

        public Long getTimeOut() {
            return timeOut;
        }

        public void setTimeOut(Long timeOut) {
            this.timeOut = timeOut;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
