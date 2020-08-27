package com.ajaxjs.app;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.app.attachment.Attachment_picture;
import com.ajaxjs.app.attachment.Attachment_pictureService;
import com.ajaxjs.framework.TestHelper;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.util.ioc.ComponentMgr;

public class TestAttachment_picture {
	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();
	}

	@Test
	public void testOwner() {
		long sz = 469557228827836416L;
		Attachment_pictureService owner = ComponentMgr.get(Attachment_pictureService.class);
		List<Attachment_picture> oo = owner.findByOwner(sz);
		for (Attachment_picture aa : oo) {
			assertNotNull(aa.getPath());
		}
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}