package com.ajaxjs.user.common.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.common.service.IResetPassword;
import com.ajaxjs.user.common.service.ResetPasswordService;

@RestController
@RequestMapping("/user/reset_password")
@InterfaceBasedController(serviceClass = ResetPasswordService.class)
public interface ResetPasswordController extends IResetPassword {
	@PostMapping("/sendRestEmail")
	@ControllerMethod("发送重置邮件")
	@Override
	Boolean sendRestEmail(@RequestParam(required = true) String email, @RequestParam(required = true) int tenantId);

	@PostMapping("/verifyTokenUpdatePsw")
	@ControllerMethod("根据邮件重置密码")
	@Override
	Boolean verifyTokenUpdatePsw(@RequestParam(required = true) String token, @RequestParam(required = true) String newPsw,
			@RequestParam(required = true) String email, @RequestParam(required = true) int tenantId);

	@PostMapping("/sendRestPhone")
	@ControllerMethod("发送手机短信")
	@Override
	Boolean sendRestPhone(@RequestParam(required = true) String phone, @RequestParam(required = true) int tenantId);

	@PostMapping("/verifySmsUpdatePsw")
	@ControllerMethod("根据短信重置密码")
	@Override
	Boolean verifySmsUpdatePsw(@RequestParam(required = true) String code, @RequestParam(required = true) String newPsw,
			@RequestParam(required = true) String phone, @RequestParam(required = true) int tenantId);
}
