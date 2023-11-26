package com.ajaxjs.iam.model;


import lombok.Data;

/**
 * 访问令牌
 */
@Data
public class AccessToken  {
    /**
     * Access Token
     */
    private String access_token;

    /**
     * 刷新 Token
     */
    private String refresh_token;

    /**
     * 有效期，以秒为单位
     */
    private Integer expires_in;

    /**
     * 权限范围
     */
    private String scope;
}
