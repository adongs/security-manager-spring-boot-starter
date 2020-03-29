package com.adongs.data;

import com.adongs.session.user.User;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author yudong
 * @version 1.0
 */
public class DefaultDataSource implements DataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataSource.class);
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

    private Users getUser(String token){
        if (token==null){return null;}
        Users user = userMap.get(token);
        if (user==null){return null;}
        if (user.getTimeOut()!=-1 && user.getTimeOut()<System.currentTimeMillis()){
            return null;
        }
        return user;
    }


    @Override
    public Optional<User> token(Optional<String> token) {
        if (token.isPresent()){
            User user = getUser(token.get());
            if (user!=null){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Set<String> roles(String token) {
        Users user = getUser(token);
        if (user!=null){
            return user.getRoles();
        }
        return null;
    }

    @Override
    public Set<String> permissions(String token) {
        Users user = getUser(token);
        if (user!=null){
            return user.getPermissions();
        }
        return null;
    }

    @Override
    public void logout(String token) {
        userMap.remove(token);
    }

    @Override
    public long updateTime(String token,long time) {
        Users user = userMap.get(token);
        if (user!=null){
            user.setTimeOut(time);
        }
        return 0;
    }

    public static class Users extends User {
        private String name;
        private Set<String> roles;
        private Set<String> permissions;
        private Long timeOut = null;


        public Users(String name, Integer id, Set<String> roles, Set<String> permissions, Long timeOut) {
            this.name = name;
            this.setId(id);
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
    }
}
