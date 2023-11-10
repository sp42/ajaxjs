package com.ajaxjs.iam.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JwtAccessToken extends AccessToken {
    /**
     * Token 类型
     */
    private String token_type = "Bearer";

    /**
     * JWT Token
     */
    private String id_token;
}
