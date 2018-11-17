package com.ajaxjs.user.role;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.user.role.service.UserRoleRoleServiceImpl;

public class TestUserRoleService {
	@Test
	public void testGetGetExistingPrime() {

		Map<String, Object> user = new HashMap<>();
		user.put("name", "foo");

		UserRoleRoleServiceImpl service = new UserRoleRoleServiceImpl();
		assertNotNull(service.create(user));
	}

}
