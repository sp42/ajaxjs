package com.ajaxjs.user.controller;

import com.ajaxjs.user.model.AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/oauth2")
public interface OAuth2Controller {
    /**
     * 获取授权码（Authorization Code）
     *
     * @param clientId    客户端标识符，表示 OAuth 客户端的唯一标识
     * @param redirectUri 重定向 URI，表示授权服务器将授权码发送到此URI
     * @param scope       作用域，表示客户端请求的权限范围
     * @param status      用于防止 CSRF 攻击（非必填）
     * @return 重定向到指定的 redirect_uri，并携带授权码
     */
    @GetMapping("/authorize")
    ModelAndView authorize(@RequestParam String clientId, @RequestParam String redirectUri, @RequestParam(required = false) String scope, @RequestParam(required = false) String status);

    @PostMapping("/token")
    AccessToken getToken(@RequestParam String clientId, @RequestParam String clientSecret, @RequestParam String code);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新用的 Token
     * @return 返回包含新的访问令牌的JSON数据，包括access_token、expires_in、token_type等字段
     */
    @PostMapping("/token/refresh")
    AccessToken refreshToken(@RequestParam String refreshToken);

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
}
