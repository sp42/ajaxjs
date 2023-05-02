package com.ajaxjs.user.controller;

import com.ajaxjs.framework.spring.easy_controller.anno.InterfaceBasedController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.user.service.RegisterService;
import com.ajaxjs.user.service.RegisterServiceImpl;

@RestController
@RequestMapping("/user/register")
@InterfaceBasedController(serviceClass = RegisterServiceImpl.class)
public interface RegisterController extends RegisterService {
}
