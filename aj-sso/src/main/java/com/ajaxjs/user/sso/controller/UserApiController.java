package com.ajaxjs.user.sso.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;

/**
 * 用户相关操作接口
 * 
 * @author Frank Cheung
 *
 */
@RestController
@RequestMapping("/user_api")
public class UserApiController extends BaseController {
	@GetMapping(produces = JSON)
	String getInfo() {
		return jsonOk();
	}
}
