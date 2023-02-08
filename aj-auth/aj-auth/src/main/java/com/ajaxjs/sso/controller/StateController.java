package com.ajaxjs.sso.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.sso.service.IStateService;
import com.ajaxjs.sso.service.StateService;

@RestController
@RequestMapping("/state")
@InterfaceBasedController(serviceClass = StateService.class)
public interface StateController extends IStateService {
    @GetMapping("isLogined")
    @ControllerMethod("是否已登录")
    @Override
    Boolean isLogined(HttpServletRequest req);

    @GetMapping("verifyToken")
    @ControllerMethod("验证 Token")
    @Override
    Boolean verify(@RequestParam String access_token);
}
