/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.sql;

import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 格式化 SQL/填充 SQL 实际值
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class JdbcUtil {
	private static final LogHelper LOGGER = LogHelper.getLog(JdbcUtil.class);

	/**
	 * 为方便单测，设一个开关
	 */
	public static boolean IS_DB_CONNECTION_AUTOCLOSE = true;

	/**
	 * 是否关闭日志打印以便提高性能
	 */
	public static boolean isClosePrintRealSql = false;

	/**
	 * 打印真实 SQL 执行语句
	 * 
	 * @param sql    SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 实际 sql 语句
	 */
	public static String printRealSql(String sql, Object[] params) {
		if (!StringUtils.hasText(sql))
			throw new IllegalArgumentException("SQL 语句不能为空！");

		if (isClosePrintRealSql)
			return null;

		if (params == null || params.length == 0) // 完整的 SQL 无须填充
			return sql;

		if (!match(sql, params))
			LOGGER.info("SQL 语句中的占位符与值参数（个数上）不匹配。SQL：{0}，\nparams:{1}", sql, Arrays.toString(params));

		if (sql.endsWith("?"))
			sql += " ";

		String[] arr = sql.split("\\?");

		for (int i = 0; i < arr.length - 1; i++) {
			Object value = params[i];
			String inSql;

			if (value instanceof Date) // 只考虑了字符串、布尔、数字和日期类型的转换
				inSql = "'" + value + "'";
			else if (value instanceof String)
				inSql = "'" + value + "'";
			else if (value instanceof Boolean)
				inSql = (Boolean) value ? "1" : "0";
			else if (value != null)
				inSql = value.toString();// number
			else
				inSql = "";

			arr[i] = arr[i] + inSql;
		}

		return String.join(" ", arr).trim();
	}

	/**
	 * ? 和参数的实际个数是否匹配
	 * 
	 * @param sql    SQL 语句，可以带有 ? 的占位符
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
	 * 将以下划线分隔的数据库字段转换为驼峰风格的字符串
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String changeColumnToFieldName(String columnName) {
		String[] array = columnName.split("_");
		StringBuffer sb = null;

		for (String cn : array) {
			cn = cn.toLowerCase();
			if (sb == null) {
				sb = new StringBuffer(cn);
				continue;
			}

			sb.append(cn.substring(0, 1).toUpperCase()).append(cn.substring(1));
		}

		return sb.toString();
	}

	/**
	 * 将驼峰风格的字符串转换为以下划线分隔的数据库字段
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String changeFieldToColumnName(String fieldName) {
		if (fieldName == null)
			return null;

		StringBuffer columnName = new StringBuffer();
		int length = fieldName.length();

		for (int i = 0; i < length; i++) {
			char c = fieldName.charAt(i);

			if ('A' <= c && 'Z' >= c)
				columnName.append("_").append((char) (c + 32));
			else
				columnName.append(fieldName.charAt(i));

		}

		return columnName.toString();
	}

	/**
	 * pageSize 转换为 MySQL 的 start 分页
	 * 
	 * @param pageNo
	 * @param limit
	 * @return
	 */
	public static int pageNo2start(int pageNo, int limit) {
		int start = (pageNo - 1) * limit;

		return (start < 0) ? 0 : start;
	}
}
