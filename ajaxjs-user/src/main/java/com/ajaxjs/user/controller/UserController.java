package com.ajaxjs.user.controller;

import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import com.ajaxjs.cms.utils.sms.SMS;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserCommonAuthService;
import com.ajaxjs.user.service.UserService;

/**
 * 用户总的控制器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/user")
@Bean
public class UserController extends AbstractUserController {
	@Resource("User_common_authService")
	private UserCommonAuthService authService;

	@Resource("UserService")
	private UserService service;
	
	@Resource("AliyunSMSSender")
	private SMS sms;
	
	@Override
	public SMS getSms() {
		return sms;
	}

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
