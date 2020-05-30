package com.ajaxjs.app;

import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.app.developer.MysqlAutoBackup;
import com.ajaxjs.app.developer.MysqlAutoBackup.MysqlExport;
import com.ajaxjs.app.developer.TomcatLogController.LogFileTailer;
import com.ajaxjs.framework.config.TestHelper;
import com.ajaxjs.orm.JdbcConnection;

public class TestDeveloper {
	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();
	}

	@Test
	public void testMySqlBackup() {
		MysqlExport m = new MysqlExport(JdbcConnection.getConnection(), "c:/temp");
		m.export();

		MysqlAutoBackup timer = new MysqlAutoBackup();
		assertNotNull(timer);
	}

	@Test
	public void testBackup() {
		String d = "C:/sp42/dev/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ajaxjs-demo/WEB-INF/lib";
		System.out.println(d.replaceAll("/", "\\\\"));
	}

	@Test
	public void testTomcatLog() {
		LogFileTailer tailer = new LogFileTailer("C:\\temp\\bar.txt", 1000, true);
		tailer.setTailing(true);
		tailer.addListener(System.out::println);
		tailer.start();
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
