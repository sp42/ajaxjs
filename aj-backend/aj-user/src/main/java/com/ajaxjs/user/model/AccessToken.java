package com.ajaxjs.user.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

@Data
public class AccessToken implements IBaseModel {
    /**
     * Access Token
     */
    private String access_token;

    /**
     * 刷新 Token
     */
    private String refresh_token;

    /**
     * 过期时间
     */
    private Long expires_in;

    /**
     * 权限范围
     */
    private String scope;
}
