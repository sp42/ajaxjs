package com.ajaxjs.database_meta;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import com.ajaxjs.database_meta.model.Database;
import com.ajaxjs.database_meta.model.TableDesc;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.util.TestHelper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.map.JsonHelper;

public class TestMeta {
	Connection conn = JdbcConnection.getConnection(
			"jdbc:mysql://58.248.254.12:3306/ajaxjs?useUnicode=true&characterEncoding=UTF-8&useSSL=false&user=root&password=gz@87654321");

//	@Test
	public void testGetDatabase() {
		DataBaseQuery dataBase = new DataBaseQuery(conn);
		String[] databaseNames = dataBase.getDatabase();

		assertNotNull(databaseNames[0]);

		TestHelper.printJson(databaseNames);
	}

//	@Test
	public void testDataBaseWithTable() {
		DataBaseQuery dataBase = new DataBaseQuery(conn);
		Database[] dataBaseWithTable = dataBase.getDataBaseWithTable();

		assertNotNull(dataBaseWithTable[0]);

		TestHelper.printJson(dataBaseWithTable);
		System.out.println(JsonHelper.toJson(dataBaseWithTable));
	}

//	@Test
	public void testDataBaseWithTableFull() {
		DataBaseQuery dataBase = new DataBaseQuery(conn);
		Database[] dataBaseWithTable = dataBase.getDataBaseWithTableFull();

		assertNotNull(dataBaseWithTable[0]);

//		TestHelper.printJson(dataBaseWithTable);
		String json = "DOC_DATA = " + JsonHelper.toJson(dataBaseWithTable);
		FileHelper.saveText("C:\\code\\aj\\aj-all\\ajaxjs\\aj-ui-widget\\database-doc\\json.js", json);
	}

	@Test
	public void testDataBaseMeta() {
		MetaQuery meta = new MetaQuery(conn);
//		System.out.println(meta.getAllVariable());
//		Map<String, String> dbSize = meta.getDbSize("ajaxjs");

		Map<String, TableDesc> tableDesc = meta.getTableDesc("ajaxjs", new String[] { "auth_access_token" });
		System.out.println(JsonHelper.toJson(tableDesc));
	}

	@After
	public void tearDown() throws SQLException {
		conn.close();
	}

}
