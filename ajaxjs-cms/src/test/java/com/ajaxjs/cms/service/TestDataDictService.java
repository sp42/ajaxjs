package com.ajaxjs.cms.service;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.DataDictService;
import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestDataDictService {
	static DataDictService service;

	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		service = (DataDictService) BeanContext.getBean("DataDictService");
	}

 

	@Test
	public void testPageList() {
		PageResult<Map<String, Object>> page;
		page = DataDictService.dao.findPagedList(0, 10);
		assertNotNull(page);
		assertNotNull(page.get(0));
		assertNotNull(page.get(0).get("name"));
		assertNotNull(page.getTotalCount());
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
