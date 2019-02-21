package com.ajaxjs.cms.user;

import static com.ajaxjs.cms.user.role.RoleUtil.getSingleKeyLock;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.AdsService;
import com.ajaxjs.cms.user.role.RoleService;
import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestRole {
	static AdsService service;

	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		service = (AdsService) BeanContext.getBean("AdsService");
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
		System.out.println(Arrays.toString(ep));
		Arrays.toString(ep);
		
	//	System.out.println(getNextPrime());
	}

	@Test
	public void testGetGetPrimeNumber() {
		assertEquals("[2, 3, 5, 7]", Arrays.toString(RoleService.getPrimeNumber(10)));
	}

	@Test
	public void testGetSingleKeyLock() {
		Set<Integer> ints = getSingleKeyLock(686070);
		assertEquals("[2, 3, 5, 7, 11]", ints.toString());
	}
	
	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
