package com.ajaxjs.cms.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.model.Ads;
import com.ajaxjs.cms.service.AdsService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.dao.MockDataSource;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.mock.TestHelper;
import com.ajaxjs.orm.JdbcConnection;

public class TestAdsService {
	static AdsService service;

	@BeforeClass
	public static void initDb() {
		ConfigService.load("c:\\project\\wyzx-pc\\src\\resources\\site_config.json");
		JdbcConnection.setConnection(MockDataSource.getTestMySqlConnection(ConfigService.getValueAsString("testServer.mysql.url"), ConfigService.getValueAsString("testServer.mysql.user"),
				ConfigService.getValueAsString("testServer.mysql.password")));
		BeanContext.init("com.ajaxjs.cms");
		service = (AdsService) BeanContext.getBean("AdsService");
	}

	static String[] names = new String[] { "招聘文员两名", "招聘会计一名" };
	static String[] content = new String[] { "1、监管应收账款、跟踪应收到期款；2、依据市场部订单进行应收账款的核算；3、工作认真,品行端正,吃苦耐劳", "1、负责部门一些日常行政事务,配合上级做好行政人事方面的工作；2、负责办理各类文件的收发、登记" };
	static String[] expr = new String[] { "一年", "两年", "三年" };

//	@Test
	public void testCreate() {
		Ads entity = new Ads();

		for (int i = 0; i < 10; i++) {
			entity.setName(TestHelper.getItem(names));
			assertNotNull(service.create(entity));
		}
	}

//	@Test
	public void testPageList() {
		PageResult<Ads> entities;
		entities = service.findPagedList(0, 10);
		System.out.println(entities);
		assertNotNull(entities.getTotalCount());
	}

	@Test
	public void testListByCatelogId() {
		List<Ads> entities;
		entities = service.findListByCatelogId(122, null);
		System.out.println(entities);
		assertNotNull(entities.size());
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
