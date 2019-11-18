package com.ajaxjs.cms.app;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestAttachment_picture{
	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
	}

	@Test
	public void testOwner() {
		long sz = 469557228827836416L;
		Attachment_pictureService owner = (Attachment_pictureService)BeanContext.getBean("Attachment_pictureService");
		List<Attachment_picture> oo = owner.findByOwner(sz);
		for (Attachment_picture aa : oo) {
			System.out.println(aa.getPath());
		}
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
		JdbcConnection.clean();
	}
}
