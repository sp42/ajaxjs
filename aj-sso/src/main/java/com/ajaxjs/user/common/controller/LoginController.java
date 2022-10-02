package com.ajaxjs.user.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.common.service.ILoginService;
import com.ajaxjs.user.common.service.LoginService;

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
	@PostMapping
	@ControllerMethod("用户登入")
	@Override
	Boolean login(@RequestParam String userID, @RequestParam String password, @RequestParam int tenantId, HttpServletRequest req);

	@PostMapping("logout")
	@ControllerMethod("用户登出")
	@Override
	Boolean logout(HttpSession session);

	@GetMapping("/log")
	@ControllerMethod("查看登录日志")
	@Override
	String listLog(int start, int limit);
}
