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
}
