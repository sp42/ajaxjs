package com.ajaxjs.cms.utils.codegenerators;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class Utils {

	public static String getName(String sqlType) {
		String[] arr = sqlType.split("，|,|\\.|。");

		return arr[0];
	}

	public static String toJavaType(String sqlType) {
		String t = "void";

		if (sqlType.indexOf("varchar") != -1 || sqlType.indexOf("char") != -1 || sqlType.indexOf("text") != -1)
			t = "String";
		else if (sqlType.indexOf("datetime") != -1)
			t = "java.util.Date";
		else if (sqlType.indexOf("bigint") != -1)
			t = "Long";
		else if (sqlType.indexOf("int") != -1 || sqlType.indexOf("date") != -1)
			t = "Integer";
		else if (sqlType.indexOf("float") != -1)
			t = "Float";
		else if (sqlType.indexOf("double") != -1)
			t = "Double";
		else if (sqlType.indexOf("decimal") != -1)
			t = "java.math.BigDecimal";

		System.out.println(sqlType+":::::::::::" + t);
		return t;
	}

	/**
	 * 将第一个字母大写
	 * 
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String firstLetterUpper(String str) {
		return Character.toString(str.charAt(0)).toUpperCase() + str.substring(1);
	}

	public static File[] getFileName(String path) {
		File file = new File(path);
		return file.listFiles();
	}

	public static void rename(File[] files, boolean isTxt2jsp) {
		for (File file : files) {
			String fileName = file.getAbsolutePath();
			if (file.exists())
				file.renameTo(new File(isTxt2jsp ? fileName.replaceAll(".txt", ".jsp") : fileName.replaceAll(".txt", ".jsp")));
		}
	}

	/**
	 * 获取磁盘真實地址。
	 * 
	 * @param cxt Web 上下文
	 * @param relativePath 相对地址
	 * @return 绝对地址
	 */
	public static String mappath(ServletContext cxt, String relativePath) {
		String absolute = cxt.getRealPath(relativePath);

		if (absolute != null)
			absolute = absolute.replace('\\', '/');
		return absolute;
	}

	/**
	 * 根据 JDBC Url 创建 MySQL 数据源对象
	 *
	 * @param url 像 "jdbc:mysql://localhost:3306/databaseName"
	 * @param user 用户名
	 * @param password 密码
	 * @return 数据源对象
	 * @throws SQLException
	 */
	public static Connection getMySqlDataSource(String url, String user, String password) {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setURL(url);
		dataSource.setUser(user);
		dataSource.setPassword(password);

		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 先检查目录是否存在，若不存在先建立
	 * 
	 * @param folders 多个目录
	 */
	public static void mkdir(String[] folders) {
		File dir;
		for (String folder : folders) {
			dir = new File(folder);
			if (!dir.exists())
				dir.mkdirs();
		}
	}
}
