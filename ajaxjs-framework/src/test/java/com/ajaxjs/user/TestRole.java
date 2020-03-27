	package com.ajaxjs.user;

import static com.ajaxjs.user.role.RoleUtil.getR;
import static com.ajaxjs.user.role.RoleUtil.getSingleKeyLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.user.role.RightConstant;
import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.user.role.RoleUtil;

public class TestRole {
//	static AdsService service;

	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
//		service = (AdsService) BeanContext.getBean("AdsService");
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

	public static void main(String[] args) {
		Class<?> clz = RightConstant.class;

		try {
			for (Field field : clz.getDeclaredFields()) {
				// 确保你要取的,是常量.否则,就不用进去这个获取操作了
				if (Modifier.isFinal(field.getModifiers())) {
					String name = field.getName();
					int value = (int) field.get(null);
				 
					assertNotNull(name + ":" + value);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
