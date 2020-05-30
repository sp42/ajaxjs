package com.ajaxjs.app.developer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ajaxjs.orm.JdbcReader;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.IoHelper;
import com.ajaxjs.util.io.ZipHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 免 mysqldump 命令备份 SQL
 * 
 * @author frank
 *
 */
public class MysqlExport {
	private static final LogHelper LOGGER = LogHelper.getLog(MysqlExport.class);

	/**
	 * 创建 MysqlExport 对象
	 * 
	 * @param conn       数据库连接对象
	 * @param saveFolder 保存目录
	 */
	public MysqlExport(Connection conn, String saveFolder) {
		String[] arr = conn.toString().split("\\?")[0].split("/");
		this.databaseName = arr[arr.length - 1];
		this.saveFolder = saveFolder;

		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}

	private static final String SQL_START_PATTERN = "-- start";

	private static final String SQL_END_PATTERN = "-- end";

	/**
	 * 执行语句
	 */
	private Statement stmt;

	/**
	 * 数据库名
	 */
	private String databaseName;

	/**
	 * 导出 SQL 的目录
	 */
	private String saveFolder;

	/**
	 * 获取当前数据库下的所有表名称
	 * 
	 * @return List\<String\> 所有表名称
	 */
	private List<String> getAllTables() {
		List<String> tables = new ArrayList<>();

		JdbcReader.rsHandle(stmt, "SHOW TABLE STATUS FROM `" + databaseName + "`;", rs -> {
			try {
				while (rs.next())
					tables.add(rs.getString("Name"));
			} catch (SQLException e) {
				LOGGER.warning(e);
			}
		});

		return tables;
	}

	/**
	 * 生成 create 语句
	 *
	 * @param table 表名
	 * @return String
	 */
	private String getTableInsertStatement(String table) {
		StringBuilder sql = new StringBuilder();

		JdbcReader.rsHandle(stmt, "SHOW CREATE TABLE `" + table + "`;", rs -> {
			try {
				while (rs.next()) {
					String qtbl = rs.getString(1), query = rs.getString(2);
					query = query.trim().replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS");

					sql.append("\n\n--");
					sql.append("\n").append(SQL_START_PATTERN).append("  table dump : ").append(qtbl);
					sql.append("\n--\n\n");
					sql.append(query).append(";\n\n");
				}

				sql.append("\n\n--\n").append(SQL_END_PATTERN).append("  table dump : ").append(table).append("\n--\n\n");
			} catch (SQLException e) {
				LOGGER.warning(e);
			}
		});

		return sql.toString();
	}

	/**
	 * 生成insert语句
	 *
	 * @param table the table to get inserts statement for
	 * @return String generated SQL insert
	 */
	private String getDataInsertStatement(String table) {
		StringBuilder sql = new StringBuilder();
		JdbcReader.rsHandle(stmt, "SELECT * FROM " + "`" + table + "`;", rs -> {
			try {
				rs.last();
//				int rowCount = rs.getRow();
//			if (rowCount <= 0)
//				return sql.toString();

				sql.append("\n--").append("\n-- Inserts of ").append(table).append("\n--\n\n");
				sql.append("\n/*!40000 ALTER TABLE `").append(table).append("` DISABLE KEYS */;\n");
				sql.append("\n--\n").append(SQL_START_PATTERN).append(" table insert : ").append(table).append("\n--\n");
				sql.append("INSERT INTO `").append(table).append("`(");

				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();

				for (int i = 0; i < columnCount; i++) {
					sql.append("`").append(metaData.getColumnName(i + 1)).append("`, ");
				}

				sql.deleteCharAt(sql.length() - 1).deleteCharAt(sql.length() - 1).append(") VALUES \n");

				rs.beforeFirst();
				while (rs.next()) {
					sql.append("(");
					for (int i = 0; i < columnCount; i++) {

						int columnType = metaData.getColumnType(i + 1);
						int columnIndex = i + 1;

						if (Objects.isNull(rs.getObject(columnIndex))) {
							sql.append("").append(rs.getObject(columnIndex)).append(", ");
						} else if (columnType == Types.INTEGER || columnType == Types.TINYINT || columnType == Types.BIT) {
							sql.append(rs.getInt(columnIndex)).append(", ");
						} else {
							String val = rs.getString(columnIndex).replace("'", "\\'");
							sql.append("'").append(val).append("', ");
						}
					}

					sql.deleteCharAt(sql.length() - 1).deleteCharAt(sql.length() - 1);
					sql.append(rs.isLast() ? ")" : "),\n");
				}
			} catch (SQLException e) {
				LOGGER.warning(e);
			}
		});

		sql.append(";\n--\n").append(SQL_END_PATTERN).append(" table insert : ").append(table).append("\n--\n");
		// enable FK constraint
		sql.append("\n/*!40000 ALTER TABLE `").append(table).append("` ENABLE KEYS */;\n");

		return sql.toString();
	}

	/**
	 * 导出所有表的结构和数据
	 *
	 * @return String
	 */
	private String exportToSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("--\n-- Generated by AJAXJS-Data");
		sql.append("\n-- Date: ").append(CommonUtil.now("d-M-Y H:m:s")).append("\n--");

		// these declarations are extracted from HeidiSQL
		sql.append("\n\n/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;").append("\n/*!40101 SET NAMES utf8 */;\n/*!50503 SET NAMES utf8mb4 */;")
				.append("\n/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;")
				.append("\n/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;");

		List<String> tables = getAllTables();

		for (String s : tables) {
			sql.append(getTableInsertStatement(s.trim()));
			sql.append(getDataInsertStatement(s.trim()));
		}

		try {
			stmt.close();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		sql.append("\n/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;")
				.append("\n/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;")
				.append("\n/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;");

		return sql.toString();
	}

	/**
	 * 执行导出
	 * 
	 * @return 打包的文件名
	 */
	public String export() {
		String fileName = "db-dump-" + CommonUtil.now("yyyy-MM-dd") + "-" + databaseName + ".sql";
		String sqlFile = saveFolder + FileHelper.SEPARATOR + fileName;

		FileHelper.saveText(sqlFile, exportToSql());
		// 压缩 zip
		ZipHelper.zip(sqlFile, sqlFile.replace(".sql", ".zip"));
		FileHelper.delete(sqlFile);

		return fileName.replace(".sql", ".zip");
	}

	private static final String CMD = "mysqldump -h %s -u %s -p %s --set-charset=UTF8 %s";

	/**
	 * 传统方式备份数据库。需要 mysqldump 命令行。
	 * 
	 * @param ip
	 * @param userName
	 * @param psw
	 * @param databaseName
	 */
	public static void exec(String ip, String userName, String psw, String databaseName) {
		try {
			Process process = Runtime.getRuntime().exec(String.format(CMD, ip, userName, psw, databaseName));
			try (InputStream in = process.getInputStream();) {
				String sql = IoHelper.byteStream2string(in);
				FileHelper.saveText("c:/temp/" + CommonUtil.now("yyyy-MM-dd_HH-mm-ss") + ".sql", sql);
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

}