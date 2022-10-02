package com.ajaxjs.user.common.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.common.service.IRegisterService;
import com.ajaxjs.user.common.service.RegisterService;

@RestController
@RequestMapping("/user/register")
@InterfaceBasedController(serviceClass = RegisterService.class)
public interface RegisterController extends IRegisterService {
	@PostMapping
	@ControllerMethod("用户注册")
	@Override
	Boolean register(@RequestParam(required = true) Map<String, Object> params, HttpServletRequest req);

	@GetMapping("/checkRepeat")
	@ControllerMethod("检查某个值是否已经存在一样的值")
	@Override
	Boolean checkRepeat(@RequestParam String field, @RequestParam String value, @RequestParam int tenantId);

}
