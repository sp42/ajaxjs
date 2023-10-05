package com.ajaxjs.user.controller;

import com.ajaxjs.user.model.AccessToken;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/oauth2")
public interface OAuth2Controller {
    @Autowired
    OAuth2Service oauthService;

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
    public ModelAndView authorize(@RequestParam String clientId, @RequestParam String redirectUri, @RequestParam(required = false) String scope, @RequestParam(required = false) String status) {
        User loginedUser = null;

        // 生成 Authorization Code
        String authorizationCode = oauthService.createAuthorizationCode(clientId, scope, loginedUser);
        String params = "?code=" + authorizationCode;

        if (StringUtils.hasText(status))
            params += "&status=" + status;

        return new ModelAndView("redirect:" + redirectUri + params);
    }

    @PostMapping("/token")
    public AccessToken getToken(@RequestParam String clientId, @RequestParam String clientSecret, @RequestParam String code) {
        return oauthService.token(clientId, clientSecret, code);
    }

    @PostMapping("/token/refresh")
    public AccessToken refreshToken(@RequestBody RefreshTokenRequest request) {
        TokenResponse response = authorizationService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token/check")
    public String checkToken(@RequestParam String token) {
        TokenInfoResponse response = authorizationService.checkToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token/revoke")
    public ResponseEntity<RevokeTokenResponse> revokeToken(@RequestParam("token") String token) {
        RevokeTokenResponse response = authorizationService.revokeToken(token);
        return ResponseEntity.ok(response);
    }
}
