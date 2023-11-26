package com.ajaxjs.iam.client.model;

import com.ajaxjs.iam.model.AccessToken;
import lombok.Data;

/**
 * 用户及 Token 信息
 */
@Data
public class UserAccessToken {
    private Long id;

    private String name;

    private AccessToken accessToken;
}
