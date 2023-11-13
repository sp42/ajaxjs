package com.ajaxjs.iam.server.controller;

import com.ajaxjs.framework.spring.response.Result;
import com.ajaxjs.iam.server.model.JwtAccessToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OIDC 控制器
 */
@RestController
@RequestMapping("/oidc")
public interface OidcController {
    /**
     * 1、发现用户未登录，返回 303，通过浏览器重定向到登录页面
     * 2、用户已登录，于是执行授权逻辑，签发授权码
     *
     * @param req  请求对象
     * @param resp 响应对象
     */
    @GetMapping("/authorization")
    void authorization(
            @RequestParam("response_type") String responseType,
            @RequestParam("client_id") String clientId, @RequestParam("redirect_uri") String redirectUri, @RequestParam(required = false) String scope, @RequestParam String state,
            HttpServletRequest req, HttpServletResponse resp);

    /**
     * 获取 Token
     *
     * @param authorization client 信息
     * @param code          授权码
     * @param state         不透明字符串
     * @param grantType     授权码流程
     * @return 令牌 Token
     */
    @PostMapping("/token")
    Result<JwtAccessToken> token(@RequestHeader String authorization, @RequestParam String code, @RequestParam String state, @RequestParam("grant_type") String grantType);
}
