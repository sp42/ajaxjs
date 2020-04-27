package com.ajaxjs.cms;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.framework.MockTest;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mock.TestHelper;
import com.ajaxjs.orm.JdbcConnection;

public class TestAdsService {
	static AdsService service;

	@BeforeClass
	public static void initDb() {
		MockTest.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		service = (AdsService) BeanContext.getBean("AdsService");
	}

	static String[] names = new String[] { "招聘文员两名", "招聘会计一名" };
	static String[] content = new String[] { "1、监管应收账款、跟踪应收到期款；2、依据市场部订单进行应收账款的核算；3、工作认真,品行端正,吃苦耐劳", "1、负责部门一些日常行政事务,配合上级做好行政人事方面的工作；2、负责办理各类文件的收发、登记" };
	static String[] expr = new String[] { "一年", "两年", "三年" };

	@Test
	public void testCreate() {
		for (int i = 0; i < 10; i++) {
			Ads entity = new Ads();
			entity.setName(TestHelper.getItem(names));
			assertNotNull(service.create(entity));
		}
	}


	@Test
	public void testListByCatelogId() {
		List<Ads> entities;
		entities = service.findListByCatalogId(122);
		assertNotNull(entities.size());
		assertNotNull(service.findPagedList(122, 0, 5, 1, true).size());
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
