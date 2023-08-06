//package com.ajaxjs.monitor.mysql;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.ajaxjs.mysql.MetaQuery;
//import com.ajaxjs.mysql.model.TableDesc;
//import com.ajaxjs.sql.JdbcConnection;
//import com.ajaxjs.util.map.BeanHelper;
//
//public class TestJdbcUtil {
//	String connUrl = "jdbc:mysql://";
//
//	Connection conn = JdbcConnection.getConnection(connUrl);
//
////	@Test
//	public void testVariable() throws SQLException {
//		Map<String, String> allVariables = MetaQuery.getAllVariable(conn);
//		System.out.println(allVariables.size());
//		for (String key : allVariables.keySet())
//			System.out.println(key + ":" + allVariables.get(key));
//		conn.close();
//	}
//
////	@Test
//	public void testGetSize() throws SQLException {
//		Map<String, String> allVariables = MetaQuery.getDbSize(conn, "ajaxjs");
//		System.out.println(allVariables.size());
//		for (String key : allVariables.keySet())
//			System.out.println(key + ":" + allVariables.get(key));
//		conn.close();
//	}
//
//	@Test
//	public void testGetTable() throws SQLException {
//		List<String> tables = MetaQuery.getTables(conn, "SHOW TABLES IN ajaxjs");
//
//		for (String table : tables)
//			System.out.println(table);
//
//		Map<String, TableDesc> tableDesc = MetaQuery.getTableDesc(conn, "ajaxjs", tables);
//		for (String key : tableDesc.keySet()) {
//			System.out.println(key + ":");
//			BeanHelper.print(tableDesc.get(key));
//		}
//
//		conn.close();
//	}
//}
