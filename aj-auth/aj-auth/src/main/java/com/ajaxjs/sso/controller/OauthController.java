package com.ajaxjs.sso.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.sso.model.ClientDetails;
import com.ajaxjs.sso.service.IOauthService;
import com.ajaxjs.sso.service.OauthService;

/**
 * OAuth 使用，有部分暂时未使用
 *
 * @author Frank Cheung
 */
@RestController
@RequestMapping("/oauth")
@InterfaceBasedController(serviceClass = OauthService.class)
public interface OauthController extends IOauthService {
    @PostMapping("/clientRegister")
    @ControllerMethod("注册客户端")
    @Override
    Boolean clientRegister(@RequestParam ClientDetails client);

    @RequestMapping("/authorizePage")
    @ControllerMethod("颁发 Token")
    @Override
    ModelAndView authorizePage(HttpServletRequest req, @RequestParam String redirectUri, @RequestParam String client_id,
                               @RequestParam String scope);

    @PostMapping("/agree")
    @ControllerMethod("颁发 Token")
    @Override
    Argee agree(HttpServletRequest req, @RequestParam String client_id, @RequestParam String scope);
}
