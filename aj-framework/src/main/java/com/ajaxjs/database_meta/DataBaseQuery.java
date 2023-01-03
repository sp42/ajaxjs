package com.ajaxjs.database_meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.ajaxjs.database_meta.model.Column;
import com.ajaxjs.database_meta.model.Database;
import com.ajaxjs.database_meta.model.Table;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.sql.util.SnowflakeId;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.regexp.RegExpUtils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

/**
 * 数据库信息查询
 * 
 * @author frank
 *
 */
public class DataBaseQuery extends BaseMetaQuery {
	public DataBaseQuery(Connection conn) {
		super(conn);
	}

	/**
	 * 获取所有库名
	 * 
	 * @param conn
	 * @return
	 */
	public String[] getDatabase() {
		List<String> list = new ArrayList<>();

		JdbcHelper.query(conn, "SHOW DATABASES", rs -> {
			try {
				while (rs.next())
					list.add(rs.getString("Database"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		return list.toArray(new String[list.size()]);
	}

	/**
	 * 两级结构，所有库和库下面所有的表名
	 * 
	 * @return
	 */
	public Database[] getDataBaseWithTable() {
		return getDataBaseWithTable(getDatabase());
	}

	static final String[] IGNORE_SYSTEM_TABLE = { "information_schema", "performance_schema", "mysql", "sys" };

	public Database[] getDataBaseWithTable(String[] databases) {
		List<Database> list = new ArrayList<>();
		TableQuery tableQuery = new TableQuery(conn);

		for (String datebaseName : databases) {

			// ignore system table
			if (StrUtil.isWordOneOfThem(datebaseName, IGNORE_SYSTEM_TABLE))
				continue;

			Database database = new Database();
			database.setUuid(SnowflakeId.get() + "");
			database.setName(datebaseName);
			database.setTables(tableQuery.getAllTableName(datebaseName));

			list.add(database);
		}

		return list.toArray(new Database[list.size()]);
	}

	/**
	 * 完整的信息，包括 CreateDDL
	 * 
	 * @return
	 */
	public Database[] getDataBaseWithTableFull(String dbName) {
		Database[] databases = getDataBaseWithTable();

		if (StringUtils.hasText(dbName)) {
			Database _database = null;
			for (Database database : databases) {
				if (database.getName().equals(dbName)) {
					_database = database;
					break;
				}
			}

			if (_database == null)
				return null; // 找不到 dbName 的
			else {
				List<Table> full = getDataBaseWithTableFull(_database.getTables(), _database.getName());
				_database.setTableInfo(full);

				return new Database[] { _database };
			}
		} else {
			for (Database database : databases) {
				List<Table> full = getDataBaseWithTableFull(database.getTables(), database.getName());
				database.setTableInfo(full);
			}

			return databases;
		}
	}

	public Database[] getDataBaseWithTableFull() {
		return getDataBaseWithTableFull(null);
	}

	/**
	 * 获取表和所有列的信息
	 * 
	 * @param tableNames 表名
	 * @param dbName     数据库名，可选
	 * @return
	 */
	public List<Table> getDataBaseWithTableFull(List<String> tableNames, String dbName) {
		List<Table> tables = new ArrayList<>();
		boolean hasDbName = StringUtils.hasText(dbName);

		JdbcHelper.stmt(conn, stmt -> {
			for (String tableName : tableNames) {
				String t = hasDbName ? dbName + "." + tableName : tableName;
				JdbcHelper.rsHandle(stmt, "SHOW CREATE TABLE " + t + "", rs -> {
					String createDDL = null;

					try {
						if (rs.next())
							createDDL = rs.getString(2);
					} catch (SQLException e) {
						e.printStackTrace();
					}

					Table table = new Table();
					tables.add(table);

					table.setUuid(SnowflakeId.get() + "");
					table.setName(tableName);
					table.setDdl((createDDL));
					table.setComment(parse(createDDL));
					table.setColumns(parseColumns(createDDL));
				});
			}
		});

		return tables;
	}

	/**
	 * 根据 DDL 语句解析各个列
	 * 
	 * @param ddl DDL 语句
	 * @return 列信息
	 */
	private List<Column> parseColumns(String ddl) {
		List<Column> list = new ArrayList<>();

		try {
			CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(ddl);
			for (ColumnDefinition col : createTable.getColumnDefinitions()) {
				Column colInfo = new Column();
				list.add(colInfo);

				colInfo.setName(col.getColumnName().replaceAll("`", ""));

				String type = col.getColDataType().toString();
				type = type.replaceAll("\\s+\\(", "("); // remove space

				colInfo.setType(type);
				String regMatch = RegExpUtils.regMatch("\\((\\d+)\\)", type, 1);
				if (StringUtils.hasText(regMatch))
					colInfo.setLength(Integer.parseInt(regMatch));

				String ddlItem = col.toString();
				String comment = RegExpUtils.regMatch("COMMENT '(.*)'", ddlItem, 1);
				colInfo.setComment(comment);
				colInfo.setIsRequired(ddlItem.contains("NOT NULL"));
			}
		} catch (JSQLParserException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 返回注释信息
	 *
	 * @param all
	 * @return
	 */
	private static String parse(String all) {
		if (all == null)
			return null;

		String comment = null;
		int index = all.indexOf("COMMENT='");
		if (index < 0)
			return "";

		comment = all.substring(index + 9);
		comment = comment.substring(0, comment.length() - 1);

		return comment;
	}

	public static String getDoc(Connection conn, String dbName) {
		DataBaseQuery d = new DataBaseQuery(conn);
		Database[] dataBaseWithTable = d.getDataBaseWithTableFull(dbName);
		String json = JsonHelper.toJson(dataBaseWithTable);

		return json;
	}

	public static void saveToDiskJson(Connection conn, String path, String dbName) {
		FileHelper.saveText(path, "DOC_DATA = " + getDoc(conn, dbName));
	}
}
