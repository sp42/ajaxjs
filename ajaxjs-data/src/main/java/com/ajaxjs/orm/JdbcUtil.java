package com.ajaxjs.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
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
				System.out.println(value);
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

	/**
	 * https://blog.csdn.net/zhengxiuchen86/article/details/81742252
	 */
	private static Map<Integer, String> jdbcTypeValues = new TreeMap<>(); // value to Name
	private static Map<Integer, Class<?>> jdbcJavaTypes = new TreeMap<>(); // jdbc type to java type

	static {
		for (Field field : Types.class.getFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				try {
					jdbcTypeValues.put((Integer) field.get(Types.class), field.getName());
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}
			}
		}

		// 初始化jdbcJavaTypes：
		jdbcJavaTypes.put(new Integer(Types.LONGNVARCHAR), String.class); // -16 字符串
		jdbcJavaTypes.put(new Integer(Types.NCHAR), String.class); // -15 字符串
		jdbcJavaTypes.put(new Integer(Types.NVARCHAR), String.class); // -9 字符串
		jdbcJavaTypes.put(new Integer(Types.ROWID), String.class); // -8 字符串
		jdbcJavaTypes.put(new Integer(Types.BIT), Boolean.class); // -7 布尔
		jdbcJavaTypes.put(new Integer(Types.TINYINT), Byte.class); // -6 数字
		jdbcJavaTypes.put(new Integer(Types.BIGINT), Long.class); // -5 数字
		jdbcJavaTypes.put(new Integer(Types.LONGVARBINARY), Blob.class); // -4 二进制
		jdbcJavaTypes.put(new Integer(Types.VARBINARY), Blob.class); // -3 二进制
		jdbcJavaTypes.put(new Integer(Types.BINARY), Blob.class); // -2 二进制
		jdbcJavaTypes.put(new Integer(Types.LONGVARCHAR), String.class); // -1 字符串
//  jdbcJavaTypes.put(new Integer(Types.NULL), String.class);    // 0 /
		jdbcJavaTypes.put(new Integer(Types.CHAR), String.class); // 1 字符串
		jdbcJavaTypes.put(new Integer(Types.NUMERIC), BigDecimal.class); // 2 数字
		jdbcJavaTypes.put(new Integer(Types.DECIMAL), BigDecimal.class); // 3 数字
		jdbcJavaTypes.put(new Integer(Types.INTEGER), Integer.class); // 4 数字
		jdbcJavaTypes.put(new Integer(Types.SMALLINT), Short.class); // 5 数字
		jdbcJavaTypes.put(new Integer(Types.FLOAT), BigDecimal.class); // 6 数字
		jdbcJavaTypes.put(new Integer(Types.REAL), BigDecimal.class); // 7 数字
		jdbcJavaTypes.put(new Integer(Types.DOUBLE), BigDecimal.class); // 8 数字
		jdbcJavaTypes.put(new Integer(Types.VARCHAR), String.class); // 12 字符串
		jdbcJavaTypes.put(new Integer(Types.BOOLEAN), Boolean.class); // 16 布尔
//  jdbcJavaTypes.put(new Integer(Types.DATALINK), String.class);   // 70 /
		jdbcJavaTypes.put(new Integer(Types.DATE), Date.class); // 91 日期
		jdbcJavaTypes.put(new Integer(Types.TIME), Date.class); // 92 日期
		jdbcJavaTypes.put(new Integer(Types.TIMESTAMP), Date.class); // 93 日期
		jdbcJavaTypes.put(new Integer(Types.OTHER), Object.class); // 1111 其他类型？
//  jdbcJavaTypes.put(new Integer(Types.JAVA_OBJECT), Object.class);  // 2000 
//  jdbcJavaTypes.put(new Integer(Types.DISTINCT), String.class);   // 2001 
//  jdbcJavaTypes.put(new Integer(Types.STRUCT), String.class);   // 2002 
//  jdbcJavaTypes.put(new Integer(Types.ARRAY), String.class);    // 2003 
		jdbcJavaTypes.put(new Integer(Types.BLOB), Blob.class); // 2004 二进制
		jdbcJavaTypes.put(new Integer(Types.CLOB), Clob.class); // 2005 大文本
//  jdbcJavaTypes.put(new Integer(Types.REF), String.class);    // 2006 
//  jdbcJavaTypes.put(new Integer(Types.SQLXML), String.class);   // 2009 
		jdbcJavaTypes.put(new Integer(Types.NCLOB), Clob.class); // 2011 大文本
	}

	public static Class<?> jdbcTypeToJavaType(int jdbcType) {
		return jdbcJavaTypes.get(jdbcType);
	}

	public static boolean isJavaNumberType(int jdbcType) {
		Class<?> type = jdbcJavaTypes.get(jdbcType);
		return (type == null) ? false : (Number.class.isAssignableFrom(type)) ? true : false;
	}

}
