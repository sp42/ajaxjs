package com.ajaxjs.app;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.app.service.section.Section;
import com.ajaxjs.app.service.section.SectionService;
import com.ajaxjs.config.TestHelper;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestSectionService {
	static SectionService service;

	@BeforeClass
	public static void initDb() {
		TestHelper.initAll("D:\\project\\leidong", "com.ajaxjs.app");
		service = BeanContext.getBean(SectionService.class);
	}

	@Test
	public void test() {
		List<Section> list = service.getListByCatalogId(30);

		System.out.println(list);
		assertNotNull(list);
	}

//	@Test
	public void testCreate() {
		assertNotNull(service);
		Section c = new Section();
		c.setCatalogId(30);
		c.setEntityUid(709360213836169216L);
		c.setTypeId(2);
		assertNotNull(service.create(c));

		c.setCatalogId(30);
		c.setEntityUid(644563412977713152L);
		c.setTypeId(1);
		assertNotNull(service.create(c));
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
