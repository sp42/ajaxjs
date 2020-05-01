package com.ajaxjs.cms;

import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.app.DataDictController;
import com.ajaxjs.app.service.SectionList;
import com.ajaxjs.app.service.SectionService;
import com.ajaxjs.framework.MockTest;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestSectionService {
	@BeforeClass
	public static void init() {
		MockTest.init("D:\\project\\bgdiving\\src\\main\\java\\site_config.json",
				"D:\\project\\bgdiving\\WebContent\\META-INF\\context.xml", "com.ajaxjs.cms");
	}

	@Test
	public void test() {
		SectionService s = BeanContext.getBean(SectionService.class);
		SectionList bean = new SectionList();
		bean.setEntryId(10L);
		bean.setCatalogId(25); // 25=banner
		bean.setEntryTypeId(DataDictController.DataDictService.ENTRY_ARTICLE);
		assertNotNull(s.create(bean));
	}

	@AfterClass
	public static void end() {
		JdbcConnection.closeDb();
	}
}
