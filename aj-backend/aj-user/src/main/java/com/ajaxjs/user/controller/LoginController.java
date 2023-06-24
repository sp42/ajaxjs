package com.ajaxjs.user.controller;

import com.ajaxjs.user.model.AppLogin;
import com.ajaxjs.user.model.TokenResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public interface LoginController {
    /**
     * 获取 AppToken
     *
     * @param login 包含应用应用 id 和应用密钥的表单
     * @return AppToken
     */
    @PostMapping("/app")
    TokenResult appLogin(AppLogin login);
}
