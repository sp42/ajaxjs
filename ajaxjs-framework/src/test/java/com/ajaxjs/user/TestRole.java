	package com.ajaxjs.user;

import static com.ajaxjs.user.role.RoleUtil.getR;
import static com.ajaxjs.user.role.RoleUtil.getSingleKeyLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.framework.config.TestHelper;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.user.role.RoleUtil;

public class TestRole {
//	static AdsService service;

	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();
//		service = (AdsService) ComponentMgr.get("AdsService");
	}

	@Test
	public void testCreate() {
		for (int i = 0; i < 10; i++) {
//			Ads entity = new Ads();
//			entity.setName(TestHelper.getItem(names));
//			assertNotNull(service.create(entity));
		}
	}

	@Test
	public void testGetGetExistingPrime() {
		Integer[] ep = new RoleService().getExistingPrime();
		Arrays.toString(ep);
	}

	@Test
	public void testGetGetPrimeNumber() {
		assertEquals("[2, 3, 5, 7]", Arrays.toString(RoleUtil.getPrimeNumber(10)));
	}

	@Test
	public void testGetSingleKeyLock() {
		Set<Integer> ints = getSingleKeyLock(686070);
		assertEquals("[2, 3, 5, 7, 11]", ints.toString());
	}

	@Test
	public void testGetR() {
		int r = getR(5, 686070);
		assertNotNull(r);
		System.out.println("::::::" + (15 & 7));
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
