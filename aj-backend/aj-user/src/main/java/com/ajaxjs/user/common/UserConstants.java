package com.ajaxjs.user.common;

import java.util.regex.Pattern;

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
     * 验证 email 是否合法正确
     */
    Pattern EMAIL_REG = Pattern.compile("^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    /**
     * 验证手机号码是否合法正确
     */
    Pattern PHONE_REG = Pattern.compile("^1[3-8]\\d{9}$");

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
