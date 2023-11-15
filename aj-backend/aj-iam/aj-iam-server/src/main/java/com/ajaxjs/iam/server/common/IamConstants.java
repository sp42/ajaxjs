package com.ajaxjs.iam.server.common;

/**
 * IAM 常量
 */
public interface IamConstants {
    /**
     * 授权模式
     */
    interface GrantType {
        /**
         * 客户端凭证（Client Credentials）授权模式
         */
        String CLIENT_CREDENTIALS = "clientCredentials";

        String REFRESH_TOKEN = "refresh_token";

        String OIDC = "authorization_code_oidc";

        String OAUTH = "authorization_code_oauth";
    }

    /**
     * accessToken的有效期为30天
     */
    Long TIME = 30 * 24 * 60 * 60 * 1000L;

    /**
     * 授权码超时 5分钟
     */
    int AUTHORIZATION_CODE_TIMEOUT = 5 * 60 * 100;

    /**
     * 应用类型 1.html web应用；2.API service应用；3.Web APi混合应用；4.原生应用；5.其他应用
     */
    enum SystemAppType {
        HTML, API_SERVICE, RPC_SERVICE, DESKTOP, NATIVE, MISC;
    }

    /**
     * 功能类型
     */
    enum FunctionType {
        /**
         * 页面地址或路由地址
         */
        URL,

        /**
         * 页面元素，如按钮
         */
        ELEMENT;
    }

    /**
     * 权限类型
     */
    enum PermissionType {
        FUNCTION, API;
    }

    /**
     * Token 关联 User 的 Key
     */
    String TOKEN_USER_KEY = "S_USER:TOKEN_USER:";

    /**
     * JWT Token 关联 User 的 Key
     */
    String JWT_TOKEN_USER_KEY = "S_USER:JWT_TOKEN_USER:";

    String USER_IN_SESSION = "USER_IN_SESSION";
}
