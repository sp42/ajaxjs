package com.ajaxjs.user.controller;

import com.ajaxjs.framework.spring.easy_controller.anno.InterfaceBasedController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.user.service.LoginService;
import com.ajaxjs.user.service.LoginServiceImpl;

/**
 * 用户注册
 *
 * @author Frank Cheung
 */
@RestController
@RequestMapping("/user/login")
@InterfaceBasedController(serviceClass = LoginServiceImpl.class)
public interface LoginController extends LoginService {

}
