package com.ajaxjs.adp.controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.ajaxjs.spring.easy_controller.ControllerMethod;

public interface ITestController {
	@GetMapping
	@ControllerMethod
	public Boolean test();
}
