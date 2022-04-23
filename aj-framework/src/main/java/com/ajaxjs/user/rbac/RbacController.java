package com.ajaxjs.user.rbac;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;

@RestController
@RequestMapping("")
public class RbacController {
	@GetMapping
	public String get(@RequestParam long id) {
		return BaseController.jsonNoOk();
	}
}
