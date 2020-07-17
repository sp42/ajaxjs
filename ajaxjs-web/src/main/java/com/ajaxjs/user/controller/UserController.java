package com.ajaxjs.user.controller;

import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserCommonAuthService;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;

/**
 * 用户总的控制器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/user")
@Component
public class UserController extends AbstractUserController {
	@Resource("User_common_authService")
	private UserCommonAuthService authService;

	@Resource("UserService")
	private UserService service;

	@Override
	public UserService getService() {
		return service;
	}

	@Override
	public BiConsumer<User, HttpServletRequest> getAfterLoginCB() {
		// TODO Auto-generated method stub
		return null;
	}
}
