package com.ajaxjs.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.service.IRegisterService;
import com.ajaxjs.user.service.RegisterService;

@RestController
@RequestMapping("/user/register")
@InterfaceBasedController(serviceClass = RegisterService.class)
public interface RegisterController extends IRegisterService {
}
