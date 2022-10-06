package com.ajaxjs.user.sso.controller;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.common.service.LoginService;
import com.ajaxjs.user.sso.service.SsoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.user.sso.model.IssueToken;
import com.ajaxjs.user.sso.model.IssueTokenWithUser;
import com.ajaxjs.user.sso.service.ISsoService;

@RestController
@RequestMapping("/sso")
@InterfaceBasedController(serviceClass = SsoService.class)
public interface SsoController extends ISsoService {
    @RequestMapping("/authorize_code")
    @ControllerMethod("获取授权码")
    @Override
    ModelAndView getAuthorizeCode(@RequestParam(required = true) String client_id, @RequestParam(required = true) String redirect_uri,
                                  @RequestParam(required = false) String scope, @RequestParam(required = false) String status, HttpServletRequest req);

    @RequestMapping("/authorize")
    @ControllerMethod("颁发 Token")
    @Override
    IssueTokenWithUser issue(@RequestParam String client_id, @RequestParam String client_secret, @RequestParam String code,
                             @RequestParam String grant_type);

    @RequestMapping("/refreshToken")
    @ControllerMethod("刷新 Token")
    @Override
    IssueToken refreshToken(@RequestParam String refresh_token);
}
