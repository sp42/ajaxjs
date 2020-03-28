package com.ajaxjs.user;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.MockTest;
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

	@BeforeClass
	public static void initDb() {
		MockTest.loadSQLiteTest("C:\\project\\new_gz88\\WebContent\\META-INF\\gz88.sqlite");

		BeanContext.init("com.ajaxjs.cms", "com.ajaxjs.user");
		BeanContext.injectBeans();

		ConfigService.load("C:\\project\\new_gz88\\src\\site_config.json");

		service = (UserService) BeanContext.getBean("UserService");
		passwordService = (UserCommonAuthService) BeanContext.getBean("User_common_authService");
	}

	// @Test
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

	@Test
	public void testLogin() {

		try {
			assertTrue(new UserController().loginByPassword("testUserName", "dfdsfsd", null) != null);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
