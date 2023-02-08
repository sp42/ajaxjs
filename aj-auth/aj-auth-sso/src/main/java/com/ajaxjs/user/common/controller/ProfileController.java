package com.ajaxjs.user.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.common.service.IProfileUser;
import com.ajaxjs.user.common.service.ProfileService;

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
