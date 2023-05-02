package com.ajaxjs.user.controller;

import com.ajaxjs.framework.spring.easy_controller.anno.InterfaceBasedController;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.user.service.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@InterfaceBasedController(serviceClass = UserServiceImpl.class)
public interface UserController extends UserService {
}
