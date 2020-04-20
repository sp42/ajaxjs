package demo.orm.dao;

import java.sql.Connection;

import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.util.resource.ScanClass;

public class TestDao {
	/**
	 * 创建 SQLite 数据库连接对象（测试用）
	 *
	 * @return 数据库连接对象
	 */
	public static Connection getTestSqliteConnection() {
		return JdbcConnection.getSqliteConnection(ScanClass.getResourcesFromClasspath("test_used_database.sqlite"));
	}

	public static void main(String[] args) {
		JdbcConnection.setConnection(getTestSqliteConnection());

		NewsDao dao = new Repository().bind(NewsDao.class);
		for (News news : dao.findList())
			System.out.println(news.getName());

		JdbcConnection.clean();
	}
}
