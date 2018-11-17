package com.ajaxjs.user.role;

import org.junit.Test;
import static com.ajaxjs.user.role.RoleUtil.*;
import static org.junit.Assert.assertEquals;

import java.util.Set;

public class TestRoleUtil {
	@Test
	public void testGetSingleKeyLock() {
		Set<Integer> ints = getSingleKeyLock(686070);
		assertEquals("[2, 3, 5, 7, 11]", ints.toString());
	}
}
