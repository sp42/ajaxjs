package com.ajaxjs.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.service.IProfileUser;
import com.ajaxjs.user.service.ProfileService;

/**
 * 用户信息控制器
 * 
 * @author Frank Cheung
 *
 */
@RestController
@RequestMapping("/user")
@InterfaceBasedController(serviceClass = ProfileService.class)
public interface ProfileController extends IProfileUser {
}
