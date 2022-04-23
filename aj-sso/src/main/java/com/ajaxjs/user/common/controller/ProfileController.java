package com.ajaxjs.user.common.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;

/**
 * 用户信息控制器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/user/profile")
public class ProfileController {
	@PostMapping(value = "/clientRegister", produces = MediaType.APPLICATION_JSON_VALUE)
	public String post() {
		return BaseController.jsonNoOk();
	}
}
