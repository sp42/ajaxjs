package com.ajaxjs.iam.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OIDC 控制器
 */
@RestController
@RequestMapping("/oidc")
public interface OidcController {
    /**
     * 1、发现用户未登录，返回303，通过浏览器重定向到登录页面
     * 2、用户已登录，于是执行授权逻辑，签发授权码
     *
     * @param req  请求对象
     * @param resp 响应对象
     */
    @GetMapping("/authorization")
    void authorization(HttpServletRequest req, HttpServletResponse resp);
}
