package com.ajaxjs.iam.server.controller;

import com.ajaxjs.iam.server.model.AccessToken;
import org.springframework.web.bind.annotation.*;

/**
 * OAuth 控制器
 */
@RestController
@RequestMapping("/oauth")
public interface OAuthController {
    /**
     * 客户端凭证获取 Token
     *
     * @param grantType    必填，且固定是 client_credentials
     * @param clientId     客户端 id
     * @param clientSecret 客户端密钥
     * @return Token
     */
    @PostMapping("/client_credentials")
    AccessToken clientCredentials(@RequestParam("grant_type") String grantType, @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret);

    /**
     * 客户端凭证获取 Token
     * 这是通过头传输 client_id/client_secret
     *
     * @param grantType     必填，且固定是 client_credentials
     * @param authorization 包含  client_id/client_secret 的头，用 Base64 编码
     * @return Token
     */
    @PostMapping("/client_credentials_basic")
    AccessToken clientCredentialsWithBasic(@RequestParam("grant_type") String grantType, @RequestHeader("Authorization") String authorization);

    /**
     * 通过 Refresh Token 刷新 Access Token
     *
     * @param grantType     必选，固定为 refresh_token
     * @param authorization 包含  client_id/client_secret 的头，用 Base64 编码
     * @param refreshToken  必选，Refresh Token
     * @return Token
     */
    @PostMapping("/refresh_token")
    AccessToken refreshToken(@RequestParam("grant_type") String grantType, @RequestHeader("Authorization") String authorization, @RequestParam("refresh_token") String refreshToken);
}
