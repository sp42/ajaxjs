package com.ajaxjs.sso.controller;

import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;
import com.ajaxjs.framework.spring.easy_controller.anno.InterfaceBasedController;
import com.ajaxjs.sso.service.StateService;
import com.ajaxjs.sso.service.StateServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/state")
@InterfaceBasedController(serviceClass = StateServiceImpl.class)
public interface StateController extends StateService {
    @GetMapping("isLogin")
    @ControllerMethod("是否已登录")
    @Override
    Boolean isLogined(HttpServletRequest req);

    @GetMapping("verifyToken")
    @ControllerMethod("验证 Token")
    @Override
    Boolean verify(@RequestParam String access_token);
}
