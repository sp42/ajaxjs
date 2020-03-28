package com.ajaxjs.user;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.framework.MockTest;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.user.role.RoleService;

public class TestUserRight {
	static RoleService service;

	@BeforeClass
	public static void initDb() {
		MockTest.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		service = (RoleService) BeanContext.getBean("UserRoleService");
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}

	@Test
	public void testUpdateResourceRightValue() throws ServiceException {
		long r = service.updateResourceRightValue(17, 8, false);
		assertEquals(262L, r);
	}
}
