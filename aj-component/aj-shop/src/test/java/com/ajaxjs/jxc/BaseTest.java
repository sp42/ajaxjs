package com.ajaxjs.jxc;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.util.TestHelper;

public abstract class BaseTest {
	@BeforeClass
	public static void initDb() {
		String projectFolder = "c:\\project\\aj-jxc\\";
		String[] packages = { "com.xyz" };

		TestHelper.init(projectFolder + "\\WebContent\\META-INF\\site_config.json", projectFolder + "\\WebContent\\META-INF\\context.xml", packages);
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
