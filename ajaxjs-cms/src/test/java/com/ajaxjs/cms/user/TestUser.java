package com.ajaxjs.cms.user;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.user.service.UserCommonAuthService;
import com.ajaxjs.cms.user.service.UserService;
import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.BeanContext;

public class TestUser {
	static UserService service;
	static UserCommonAuthService passwordService;

	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		service = (UserService) BeanContext.getBean("UserService");
		passwordService = (UserCommonAuthService) BeanContext.getBean("UserCommonAuthService");
	}

	@Test
	public void testRegister() {
		User user = new User();
		user.setName("testUserName");
		UserCommonAuth password = new UserCommonAuth();
		password.setPassword("dfdsfsd");
		
		try {
			service.register(user, password);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
