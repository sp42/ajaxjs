package test.com.ajaxjs.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.jdbc.Helper;

public class TestHelper {
	public static String perRecordSql = "SELECT %s, name FROM %s WHERE createDate < %s ORDER BY createDate DESC LIMIT 1";
	public static String nextRecordSql = "SELECT %s, name FROM %s WHERE createDate > %s ORDER BY createDate ASC LIMIT 1";

	@Test
	public void testQueryWithCallback(Connection conn, String tablename, final String id, long datetime) {
		Map<String, Map<String, String>> map = new HashMap<>();

		final Map<String, String> perRecord = new HashMap<>();
		map.put("perRecord", perRecord);
		String _perRecordSql = String.format(nextRecordSql, id, tablename, datetime);

		Helper db = new Helper();
		db.queryWithCallback(conn, _perRecordSql, new Helper.Callback() {
			@Override
			public Object doIt(ResultSet resultset) throws SQLException {
				perRecord.put(id, resultset.getString(id));
				perRecord.put("name", resultset.getString("name"));
				return null;
			}
		});

		final Map<String, String> nextRecord = new HashMap<>();
		map.put("nextRecord", nextRecord);
		String _nextRecordSql = String.format(perRecordSql, id, tablename, datetime);
		;
		db.queryWithCallback(conn, _nextRecordSql, new Helper.Callback() {
			@Override
			public Object doIt(ResultSet resultset) throws SQLException {
				nextRecord.put(id, resultset.getString(id));
				nextRecord.put("name", resultset.getString("name"));
				return null;
			}
		});
	}

}
