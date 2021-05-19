package com.ajaxjs.user;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.user.login.LoginService;
import com.ajaxjs.user.register.RegisterService;
import com.ajaxjs.util.TestHelper;
import com.ajaxjs.util.ioc.ComponentMgr;

public class TestUser {

	String userName = TestHelper.getUserName(), psw = "dfdsfsd";

	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();
	}

	@Test
	public void testRegister() {
		RegisterService service = ComponentMgr.get(RegisterService.class);
		User user = new User();
		user.setName(userName);

		Map<String, Object> map = new HashMap<>();
		map.put("password", psw);

		try {
			service.registerByPsw(user, map);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLogin() {
		LoginService service = ComponentMgr.get(LoginService.class);

		try {
			assertTrue(service.loginByPassword(userName, psw, null) != null);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
