package com.ajaxjs.jdbc;

import java.sql.*;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

public class Jdbc7 {
// https://docs.oracle.com/javase/tutorial/jdbc/basics/cachedrowset.html
	// https://db.apache.org/derby/docs/10.9/ref/rrefjdbc4_1summary.htmlWR9QU-EV77Y-T8Q6W-PX7W2-4ZM25
	
	//http://download.oracle.com/otn-pub/jcp/jdbc-4_1-mrel-spec/jdbc4.1-fr-spec.pdf?AuthParam=1465096926_08a5ed33264923a8a52d98cd8df37692
	public static void create() {

		try (JdbcRowSet jdbcRs = RowSetProvider.newFactory().createJdbcRowSet();) {

			// 创建一个 JdbcRowSet 对象，配置数据库连接属性
			jdbcRs.setUrl("jdbc:derby:helloDB;");
			// jdbcRs.setUsername(username);
			// jdbcRs.setPassword(password);
			jdbcRs.setCommand("select name from hellotable");

			jdbcRs.execute();
			while (jdbcRs.next()) {
				System.out.println(jdbcRs.getString(1));
			}
			// delete the table
			// s.execute("drop table hellotable");
			// System.out.println("Dropped table hellotable");

			// rs.close();

			// DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			// 通过应用 JDBC 4.0, 您现在不需太多代码即可以获取及遍历异常链在以往的版本中, 您在遍历异常链时，必须手工的调用
			// getNextException 方法才能得到相同的效果
			for (Throwable ex : e) {
				System.err.println("Error encountered: " + ex);
			}
		}
		System.out.println("SimpleApp finished");
	}

	public static void main(String[] args) {
		create();

		//
		// try {
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
 
}
