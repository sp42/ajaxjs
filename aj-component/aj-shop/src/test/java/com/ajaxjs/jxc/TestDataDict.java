package com.ajaxjs.jxc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.util.TestHelper;

public class TestDataDict {
	@BeforeClass
	public static void initDb() {
		String projectFolder = "c:\\project\\aj-jxc\\";
		String[] packages = { "com.xyz" };

		TestHelper.init(projectFolder + "\\WebContent\\META-INF\\site_config.json", projectFolder + "\\WebContent\\META-INF\\context.xml", packages);
	}

	@Test
	public void get() {
		DataDict.create("计量单位主文件", "EA", "个");
		DataDict.create("计量单位主文件", "Kg", "公斤");
		DataDict.create("计量单位主文件", "M", "米");
		DataDict.getMap("计量单位主文件");
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
