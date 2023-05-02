package com.ajaxjs.user.controller;

import com.ajaxjs.framework.spring.easy_controller.anno.InterfaceBasedController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.user.service.ResetPassword;
import com.ajaxjs.user.service.ResetPasswordServiceImpl;

@RestController
@RequestMapping("/user/reset_password")
@InterfaceBasedController(serviceClass = ResetPasswordServiceImpl.class)
public interface ResetPasswordController extends ResetPassword {
}
