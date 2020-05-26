package com.ajaxjs.user;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.config.TestHelper;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.user.controller.UserController;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.UserCommonAuth;
import com.ajaxjs.user.service.UserCommonAuthService;
import com.ajaxjs.user.service.UserService;

public class TestUser {
	static UserService service;
	static UserCommonAuthService passwordService;

	String userName = TestHelper.getUserName(), psw = "dfdsfsd";

	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();

		service = BeanContext.getBean(UserService.class);
		passwordService = BeanContext.getBean(UserCommonAuthService.class);
	}

	@Test
	public void testRegister() {
		User user = new User();
		user.setName(userName);
		UserCommonAuth password = new UserCommonAuth();
		password.setPassword(psw);

		try {
			service.register(user, password);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLogin() {
		try {
			assertTrue(new UserController().loginByPassword(userName, psw, null) != null);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
