package com.ajaxjs.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.service.IResetPassword;
import com.ajaxjs.user.service.ResetPasswordService;

@RestController
@RequestMapping("/user/reset_password")
@InterfaceBasedController(serviceClass = ResetPasswordService.class)
public interface ResetPasswordController extends IResetPassword {
}
