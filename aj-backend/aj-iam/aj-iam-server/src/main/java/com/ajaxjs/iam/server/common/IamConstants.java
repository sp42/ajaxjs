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
    }

    /**
     * accessToken的有效期为30天
     */
    Long TIME = 30 * 24 * 60 * 60 * 1000L;

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

    String USER_IN_SESSION = "USER_IN_SESSION";
}
