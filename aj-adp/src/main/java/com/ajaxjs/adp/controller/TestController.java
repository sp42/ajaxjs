package com.ajaxjs.adp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.adp.data_service.TestService;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;

@RestController
@RequestMapping("/test")
@InterfaceBasedController(serviceClass = TestService.class)
public interface TestController extends ITestController {

}
