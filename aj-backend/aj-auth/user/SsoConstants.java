package com.ajaxjs.user;

/**
 * 常量
 *
 * @author Frank Cheung
 */
public interface SsoConstants {
    /**
     * 用户信息在session中存储的变量名
     */
    public static final String SESSION_USER = "SESSION_USER";

    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * 授权页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_AUTH_REDIRECT_URL = "SESSION_AUTH_REDIRECT_URL";

    // 主库
    public final static String OAUTH_DB_MASTER = "oauth_master";

    // 从库
    public final static String OAUTH_DB_SLAVE = "oauth_slave";

    // 缓存前缀
    public static final String CACHE_CODE = "cache_code_";
}
