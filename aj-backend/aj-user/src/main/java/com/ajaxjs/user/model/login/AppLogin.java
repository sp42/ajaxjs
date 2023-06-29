package com.ajaxjs.user.model.login;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * App 登录所需信息
 */
@Data
public class AppLogin implements IBaseModel {
    /**
     * 应用 id
     */
    private String clientId;

    /**
     * 应用密钥
     */
    private String clientSecret;
}
