package com.ajaxjs.user.common;

/**
 * 用户常量
 */
public interface UserConstants {
    String REDIS_PREFIX = "USER:";

    /**
     * 性别
     */
    enum Gender {
        MALE, FEMALE, UNKNOWN
    }

    String USER_KEY = "USER_INFO";
}
