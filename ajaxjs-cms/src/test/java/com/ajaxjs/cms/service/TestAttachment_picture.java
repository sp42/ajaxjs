package com.ajaxjs.cms.service;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.model.Attachment_picture;
import com.ajaxjs.cms.service.Attachment_pictureService;
import com.ajaxjs.framework.dao.MockDataSource;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestAttachment_picture{
	@BeforeClass
	public static void initDb() {
		JdbcConnection.setConnection(MockDataSource.getTestMySqlConnection(
				"jdbc:mysql://115.28.242.232/zyjf?useUnicode=true&amp;characterEncoding=UTF-8&amp;useSSL=false", "root",
				"root123abc"));
		BeanContext.init("com.ajaxjs.cms");
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
