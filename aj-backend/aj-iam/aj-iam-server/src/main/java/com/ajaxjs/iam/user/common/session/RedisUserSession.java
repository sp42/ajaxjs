package com.ajaxjs.iam.user.common.session;


import com.ajaxjs.iam.user.model.User;

/**
 * 基于 Redis 的用户会话管理
 * TODO
 */
public class RedisUserSession implements UserSession {
    @Override
    public void put(String key, User user) {

    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void setExpires(int minutes) {

    }

    @Override
    public User getUserFromSession() {
        return null;
    }

    @Override
    public void removeAllUser() {

    }
}
