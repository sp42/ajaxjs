package com.ajaxjs.user.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * OAuth 实现 SSO
 */
@RestController
@RequestMapping("/oauth2_sso")
public class OAuthUserController {
    /**
     * 检测用户是否已经登录
     *
     * @return true 表示已登录
     */
    @GetMapping("/is_login")
    public Boolean isLogin() {
        return false;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String loginId, @RequestParam String password, @RequestParam String returnUrl) {
        return new ModelAndView("redirect:" + returnUrl);
    }
}
