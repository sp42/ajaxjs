package com.ajaxjs.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.service.ILoginService;
import com.ajaxjs.user.service.LoginService;

/**
 * 用户注册
 * 
 * @author Frank Cheung
 *
 */
@RestController
@RequestMapping("/user/login")
@InterfaceBasedController(serviceClass = LoginService.class)
public interface LoginController extends ILoginService {

}
