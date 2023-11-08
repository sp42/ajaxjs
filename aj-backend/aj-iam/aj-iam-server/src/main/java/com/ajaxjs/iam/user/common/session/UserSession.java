package com.ajaxjs.iam.user.common.session;


import com.ajaxjs.iam.user.model.User;

/**
 * 用户会话管理
 */
public interface UserSession {
    /**
     * 用户会话中键名称
     */
    String SESSION_KEY = "S_USER:LOGIN_SESSION:";

    /**
     * 保存用户到 Session
     *
     * @param key  键
     * @param user 用户
     */
    void put(String key, User user);

    /**
     * 删除某个用户
     *
     * @param key 键
     */
    void delete(String key);

    /**
     * 设置会话有效期
     *
     * @param minutes 分钟
     */
    void setExpires(int minutes);

    /**
     * 删除会话中所有的用户
     */
    void removeAllUser();
}
