package com.ajaxjs.orm;

import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ajaxjs.util.logger.LogHelper;

public class JdbcUtil {
	private static final LogHelper LOGGER = LogHelper.getLog(JdbcUtil.class);

	/**
	 * 是否关闭日志打印以便提高性能
	 */
	public static boolean isClosePrintRealSql = false;

	/**
	 * 在开发过程，SQL语句有可能写错，如果能把运行时出错的 SQL 语句直接打印出来，那对排错非常方便，因为其可以直接拷贝到数据库客户端进行调试。
	 * 
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 实际 sql 语句
	 */
	public static String printRealSql(String sql, Object[] params) {
		if (isClosePrintRealSql)
			return null;

		if (params == null || params.length == 0) // 完整的 SQL 无须填充
			return sql;

		if (!match(sql, params)) {
			LOGGER.info("SQL 语句中的占位符与值参数（个数上）不匹配。SQL：{0}，\nparams:{1}", sql, Arrays.toString(params));
		}

		if (sql.endsWith("?"))
			sql += " ";

		String[] arr = sql.split("\\?");

		for (int i = 0; i < arr.length - 1; i++) {
			Object value = params[i];

			String inSql;
			if (value instanceof Date) {
				inSql = "'" + value + "'";
			} else if (value instanceof String) {
				inSql = "'" + value + "'";
			} else if (value instanceof Boolean) {
				inSql = (Boolean) value ? "1" : "0";
			} else {
				// number
				inSql = value.toString();
			}

			arr[i] = arr[i] + inSql;
		}

		return String.join(" ", arr).trim();

//		int cols = params.length;
//		Object[] values = new Object[cols];
//		System.arraycopy(params, 0, values, 0, cols);
	}

	/**
	 * ? 和参数的实际个数是否匹配
	 * 
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return true 表示为 ? 和参数的实际个数匹配
	 */
	private static boolean match(String sql, Object[] params) {
		if (params == null || params.length == 0)
			return true; // 没有参数，完整输出

		Matcher m = Pattern.compile("(\\?)").matcher(sql);
		int count = 0;
		while (m.find())
			count++;

		return count == params.length;
	}

	/**
	 * 简单格式化 SQL，当前对 SELECT 语句有效
	 * 
	 * @param sql SELECT 语句
	 * @return 美化后的 SQL
	 */
	public static String formatSql(String sql) {
		String separator = System.getProperty("line.separator");
		sql = '\t' + sql;
		sql = sql.replaceAll("(?i)SELECT\\s+", "SELECT "); // 统一大写
		sql = sql.replaceAll("\\s+(?i)FROM", separator + "\tFROM");
		sql = sql.replaceAll("\\s+(?i)WHERE", separator + "\tWHERE");
		sql = sql.replaceAll("\\s+(?i)GROUP BY", separator + "\tGROUP BY");
		sql = sql.replaceAll("\\s+(?i)ORDER BY", separator + "\tORDER BY");
		sql = sql.replaceAll("\\s+(?i)LIMIT", separator + "\tLIMIT");
		sql = sql.replaceAll("\\s+(?i)DESC", " DESC");
		sql = sql.replaceAll("\\s+(?i)ASC", " ASC");

		return sql;
	}
}
