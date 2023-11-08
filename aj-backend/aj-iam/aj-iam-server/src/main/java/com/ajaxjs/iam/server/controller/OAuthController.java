package com.ajaxjs.iam.server.controller;

import com.ajaxjs.iam.server.model.AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * OAuth 控制器
 */
@RestController
@RequestMapping("/oauth")
public interface OAuthController {
    /**
     * 授权同意页面
     * 入参跟 authorize() 的一样
     *
     * @param clientId    客户端标识符，表示 OAuth 客户端的唯一标识
     * @param redirectUri 重定向 URI，表示授权服务器将授权码发送到此 URI
     * @param scope       作用域，表示客户端请求的权限范围
     * @param status      用于防止 CSRF 攻击（非必填）
     * @return 跳转
     */
    @GetMapping("/agree")
    ModelAndView agree(@RequestParam String clientId, @RequestParam String redirectUri, @RequestParam(required = false) String scope, @RequestParam(required = false) String status);

    /**
     * 获取授权码（Authorization Code）
     *
     * @param clientId    客户端标识符，表示 OAuth 客户端的唯一标识
     * @param redirectUri 重定向 URI，表示授权服务器将授权码发送到此 URI
     * @param scope       作用域，表示客户端请求的权限范围
     * @param status      用于防止 CSRF 攻击（非必填）
     * @return 重定向到指定的 redirect_uri，并携带授权码
     */
    @GetMapping("/authorize")
    ModelAndView authorize(@RequestParam String clientId, @RequestParam String redirectUri, @RequestParam(required = false) String scope, @RequestParam(required = false) String status);

    @PostMapping("/token")
    AccessToken getToken(@RequestParam String clientId, @RequestParam String clientSecret, @RequestParam String code);

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
     * 这是通过头传输 client_id/client_secret
     *
     * @param grantType    必选，固定为 refresh_token
     * @param clientId     客户端 id
     * @param clientSecret 客户端密钥
     * @param refreshToken 必选，Refresh Token
     * @return Token
     */
    @PostMapping("/refresh_token")
    AccessToken refreshToken(@RequestParam("grant_type") String grantType,
                             @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret,
                             @RequestParam("refresh_token") String refreshToken);

    /**
     * 通过 Refresh Token 刷新 Access Token
     * 这是通过头传输 client_id/client_secret
     *
     * @param grantType     必选，固定为 refresh_token
     * @param authorization 包含  client_id/client_secret 的头，用 Base64 编码
     * @param refreshToken  必选，Refresh Token
     * @return Token
     */
    @PostMapping("/refresh_token_basic")
    AccessToken refreshTokenWithBasic(@RequestParam("grant_type") String grantType, @RequestHeader("Authorization") String authorization, @RequestParam("refresh_token") String refreshToken);

    /**
     * 验证访问令牌
     *
     * @param token AccessToken
     * @return 返回包含访问令牌信息的JSON数据
     */
    @PostMapping("/token/check")
    String checkToken(@RequestParam String token);

    /**
     * 撤销访问令牌
     *
     * @param token AccessToken
     * @return 是否成功
     */
    @PostMapping("/token/revoke")
    Boolean revokeToken(@RequestParam String token);

    /**
     * @param clientId     应用 id
     * @param clientSecret 应用密钥
     * @return 应用的 AccessToken
     */
    @PostMapping("/app_login")
    AccessToken appLogin(@RequestParam String clientId, @RequestParam String clientSecret);

    @PostMapping("/app_login/head")
    AccessToken appLogin(@RequestParam String clientId);
}
