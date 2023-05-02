package com.ajaxjs.sso.controller;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;
import com.ajaxjs.framework.spring.easy_controller.anno.InterfaceBasedController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.sso.model.IssueToken;
import com.ajaxjs.sso.model.IssueTokenWithUser;
import com.ajaxjs.sso.service.SsoService;
import com.ajaxjs.sso.service.SsoServiceImpl;

@RestController
@RequestMapping("/sso")
@InterfaceBasedController(serviceClass = SsoServiceImpl.class)
public interface SsoController extends SsoService {
    @RequestMapping("/authorize_code")
    @ControllerMethod("获取授权码")
    @Override
    ModelAndView getAuthorizeCode(@RequestParam String client_id, @RequestParam String redirect_uri,
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
