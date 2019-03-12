package com.ajaxjs.cms.user;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.user.role.RoleService;
import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestUserRight {
	static RoleService service;

	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
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
