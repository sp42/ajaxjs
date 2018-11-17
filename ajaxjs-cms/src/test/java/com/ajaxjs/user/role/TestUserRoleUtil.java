package com.ajaxjs.user.role;

import org.junit.Test;

import com.ajaxjs.user.role.service.UserRoleRoleServiceImpl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

public class TestUserRoleUtil {
	@Test
	public void testGetGetExistingPrime() {
		Integer[] ep = new UserRoleRoleServiceImpl().getExistingPrime();
		System.out.println(Arrays.toString(ep));
		Arrays.toString(ep);
		
	//	System.out.println(getNextPrime());
		
	}

	@Test
	public void testGetGetPrimeNumber() {
		assertEquals("[2, 3, 5, 7]", Arrays.toString(UserRoleRoleServiceImpl.getPrimeNumber(10)));
	}
}
