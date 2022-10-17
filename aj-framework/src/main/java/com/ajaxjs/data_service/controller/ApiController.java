package com.ajaxjs.data_service.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.data_service.service.IApiService;
import com.ajaxjs.spring.easy_controller.ControllerMethod;

public interface ApiController extends IApiService {
	@GetMapping
	@ControllerMethod("数据服务 GET")
	@Override
	String get(HttpServletRequest req);

	@PostMapping
	@ControllerMethod("数据服务 创建实体")
	@Override
	String post(@RequestParam Map<String, Object> formPostMap, HttpServletRequest req);
}
