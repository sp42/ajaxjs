package com.ajaxjs.user.controller;

import com.ajaxjs.user.model.AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * OAuth 实现 SSO
 */
@RestController
@RequestMapping("/oauth2_sso")
public interface OAuthUserController {
    /**
     * 检测用户是否已经登录
     *
     * @return true 表示已登录
     */
    @GetMapping("/is_login")
    Boolean isLogin();

    /**
     * 普通用户登录
     *
     * @param loginId   登录账号，用户标识，可以是 username/email/phone 中的一种，后台自动判断
     * @param password  密码
     * @param returnUrl 跳转地址
     * @return 若成功登录跳转
     */
    @PostMapping("/login")
    ModelAndView login(@RequestParam String loginId, @RequestParam String password, @RequestParam String returnUrl);

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    ModelAndView logout(@RequestParam String returnUrl);

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
