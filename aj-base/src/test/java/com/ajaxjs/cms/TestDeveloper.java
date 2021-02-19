package com.ajaxjs.cms;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.TestHelper;
import com.ajaxjs.cms.common.developer.CalculateRows;
import com.ajaxjs.cms.common.developer.MysqlAutoBackup;
import com.ajaxjs.cms.common.developer.MysqlAutoBackup.MysqlExport;
import com.ajaxjs.cms.common.developer.TomcatLogController.LogFileTailer;
import com.ajaxjs.sql.JdbcConnection;

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
	public void testCalculateRows() {
		File f = new File("D:\\project\\ajaxjs-web\\src\\main\\java\\com\\ajaxjs\\web\\captcha\\CaptchaController.java"); // 目录
		String type = ".java";// 查找什么类型的代码，如".java"就是查找以java开发的代码量，".php"就是查找以PHP开发的代码量
//		treeFile(f, type);
		CalculateRows.countFile(f);
		Logger.getGlobal().info("路径：" + f.getPath());
		Logger.getGlobal().info(type + "类数量：" + CalculateRows.classcount);
		Logger.getGlobal().info("代码数量：" + CalculateRows.writeLines);
		Logger.getGlobal().info("注释数量：" + CalculateRows.commentLines);
		Logger.getGlobal().info("空行数量：" + CalculateRows.normalLines);

		if (CalculateRows.classcount == 0)
			Logger.getGlobal().info("代码平均数量:" + 0);
		else
			Logger.getGlobal().info("代码平均数量:" + CalculateRows.writeLines / CalculateRows.classcount);

		Logger.getGlobal().info("总 行数量：" + CalculateRows.allLines);
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
