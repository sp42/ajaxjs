package com.ajaxjs.sso.controller;

import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;
import com.ajaxjs.framework.spring.easy_controller.anno.InterfaceBasedController;
import com.ajaxjs.sso.model.ClientDetails;
import com.ajaxjs.sso.service.OauthService;
import com.ajaxjs.sso.service.OauthServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * OAuth 使用，有部分暂时未使用
 *
 * @author Frank Cheung
 */
@RestController
@RequestMapping("/oauth")
@InterfaceBasedController(serviceClass = OauthServiceImpl.class)
public interface OauthController extends OauthService {
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
