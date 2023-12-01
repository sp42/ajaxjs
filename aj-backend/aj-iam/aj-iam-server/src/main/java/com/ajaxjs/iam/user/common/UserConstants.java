package com.ajaxjs.iam.user.common;

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

    /**
     * 授权码有效时间
     */
    int AUTHORIZATION_CODE_TIMEOUT = 5 * 60 * 1000;

    interface LoginIdType {
        int PSW_LOGIN_ID = 1;
        int PSW_LOGIN_EMAIL = 2;
        int PSW_LOGIN_PHONE = 4;
    }

    /**
     * 登录类型
     *
     * @author Frank Cheung
     */
    interface LoginType {
        /**
         * 普通密码账号
         */
        int PASSWORD = 1;

        /**
         * 微信
         */
        int WECHAT = 2;

        /**
         * 微信小程序
         */
        int WECHAT_APPLET = 3;
    }
}
