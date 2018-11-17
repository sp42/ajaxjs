package com.ajaxjs.user.role;

import org.junit.Test;

import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.user.role.service.UserRoleRoleServiceImpl;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

public class TestUserRoleService {
	@Test
	public void testGetGetExistingPrime() {

		Map<String, Object> user = new HashMap<>();
		user.put("name", "foo");

		UserRoleRoleServiceImpl service = new UserRoleRoleServiceImpl();
		try {
			assertNotNull(service.create(user));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
