package com.ajaxjs.user;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.util.TestHelper;
import com.ajaxjs.util.ioc.ComponentMgr;

public class TestUserRight {
	static RoleService service;

	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();
		service = (RoleService) ComponentMgr.get("UserRoleService");
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
