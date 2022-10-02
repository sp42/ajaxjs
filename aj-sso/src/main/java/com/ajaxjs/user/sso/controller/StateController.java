package com.ajaxjs.user.sso.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.sso.service.IStateService;
import com.ajaxjs.user.sso.service.StateService;

/**
 * 获取用户是否登录、Token 是否合法的控制器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/state")
@InterfaceBasedController(serviceClass = StateService.class)
public interface StateController extends IStateService {
	@GetMapping("isLogined")
	@Override
	Boolean isLogined(HttpServletRequest req);

	@GetMapping("verifyToken")
	@Override
	Boolean verify(@RequestParam String access_token);
}
