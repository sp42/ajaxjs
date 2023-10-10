package com.ajaxjs.user.model;

/**
 * 用户常量
 *
 * @author Frank Cheung sp42@qq.com
 */
public interface UserConstants {
    /**
     * 性别
     */
    enum Gender {
        /**
         * 男
         */
        MALE,
        /**
         * 女
         */
        FEMALE,

        /**
         * 未知
         */
        UNKNOWN
    }

    /**
     * Session 中获取 user 的 key
     */
    public static final String USER_SESSION_KEY = "USER";

    public static final String LOGIN_PASSED = "PASSED";

    interface Login {
        public static final int PSW_LOGIN_ID = 1;
        public static final int PSW_LOGIN_EMAIL = 2;
        public static final int PSW_LOGIN_PHONE = 4;

        /**
         * 登录类型
         *
         * @author Frank Cheung
         */
        interface LoginType {
            /**
             * 普通密码账号
             */
            public static final int PASSWORD = 1;

            /**
             * 微信
             */
            public static final int WECHAT = 2;

            /**
             * 微信小程序
             */
            public static final int WECHAT_APPLET = 3;
        }
    }
}
