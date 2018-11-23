package com.ajaxjs.cms.service;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mock.DBConnection;
import com.ajaxjs.mock.TestHelper;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.PageResult;

public class TestHrService {
	static HrService service;

	@BeforeClass
	public static void initDb() {
		DBConnection.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		service = (HrService) BeanContext.getBean("HrService");
	}

	static String[] names = new String[] { "招聘文员两名", "招聘会计一名" };
	static String[] content = new String[] { "1、监管应收账款、跟踪应收到期款；2、依据市场部订单进行应收账款的核算；3、工作认真,品行端正,吃苦耐劳", "1、负责部门一些日常行政事务,配合上级做好行政人事方面的工作；2、负责办理各类文件的收发、登记" };
	static String[] expr = new String[] { "一年", "两年", "三年" };


	@Test
	public void testCreate() {
		for (int i = 0; i < 10; i++) {
			Map<String, Object> entity = new HashMap<>();
			entity.put("name", TestHelper.getItem(names));
			entity.put("content", TestHelper.getItem(content));
			entity.put("expr", TestHelper.getItem(expr));
			assertNotNull(service.create(entity));
		}
	}

	@Test
	public void testPageList() {
		PageResult<Map<String, Object>> page = service.findPagedList(0, 10);
		assertNotNull(page.getTotalCount());
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
