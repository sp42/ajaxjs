package com.ajaxjs.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.mysql.model.TableColumns;
import com.ajaxjs.mysql.model.TableDesc;
import com.ajaxjs.mysql.model.TableIndex;
import com.ajaxjs.util.logger.LogHelper;

public class MetaQuery extends BaseMetaQuery {
	private static final LogHelper LOGGER = LogHelper.getLog(MetaQuery.class);

	/**
	 * 查询某个变量
	 *
	 * @param connect 数据库连接对象
	 * @param name
	 * @param sql     要执行的 SQL 语句
	 */
	public static String getVariable(Connection connect, String name, String sql) {
		try (Statement st = connect.createStatement(); ResultSet result = st.executeQuery(sql);) {
			while (result.next())
				return result.getString(name);

		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return null;
	}

	/**
	 * 获取所有变量
	 * 
	 * @param connect 数据库连接对象
	 * @return
	 */
	public static Map<String, String> getAllVariable(Connection connect) {
		return getVariables(connect, "SHOW VARIABLES");
	}

	/**
	 * 
	 * @param connect 数据库连接对象
	 * @param sql     要执行的 SQL 语句
	 * @return
	 */
	public static Map<String, String> getVariables(Connection connect, String sql) {
		return getMapResult(connect, sql, (rs, map) -> {
			try {
				String name = rs.getString(1);
				String value = rs.getString(2);
				map.put(name, value);
			} catch (SQLException e) {
				LOGGER.warning(e);
			}
		});
	}

	/**
	 * 获取数据库的大小
	 *
	 * @param connect  数据库连接对象
	 * @param database 数据库名
	 * @return
	 */
	public static Map<String, String> getDbSize(Connection connect, String database) {
		String sql = "SELECT table_schema, concat(truncate(sum(max_data_length)/1024/1024,2),'mb') AS max_data_size, "
				+ "sum( data_length + index_length ) / 1024 / 1024 AS total_mb,sum( data_length ) / 1024 / 1024 AS data_mb, "
				+ "concat(truncate(sum(data_free)/1024/1024,2),'mb') as data_free, sum( index_length ) / 1024 / 1024 AS index_mb, "
				+ "count( * ) AS TABLES,curdate( ) AS today FROM information_schema.TABLES where table_schema = '" + database + "' GROUP BY table_schema ORDER BY 2 DESC ;";

		return getMapResult(connect, sql, (rs, map) -> {
			String name = null, value;

			try {
				ResultSetMetaData meta = rs.getMetaData();
				int count = meta.getColumnCount();

				for (int i = 1; i <= count; i++) {
					if (i == 1)
						rs.next();

					name = meta.getColumnName(i);
					value = rs.getString(name);
					map.put(name, value);
				}
			} catch (SQLException e) {
				LOGGER.warning(e);
			}
		}, false);
	}

	/**
	 * 获取某个库下的所有表信息
	 *
	 * @param connect 数据库连接对象
	 * @param sql     要执行的 SQL 语句 例如 SHOW TABLES IN xxx
	 * @return
	 */
	public static List<String> getTables(Connection connect, String sql) {
		return getResult(connect, sql, (rs) -> {
			try {
				return rs.getString(1);
			} catch (SQLException e) {
				LOGGER.warning(e);
				return null;
			}
		}, String.class);
	}

	/**
	 * 获取表的详情信息
	 *
	 * @param connect  数据库连接对象
	 * @param database 数据库名
	 * @param tables 表名集合
	 * @return
	 */
	public static Map<String, TableDesc> getTableDesc(Connection connect, String database, List<String> tables) {
		String sqlIn = "";

		for (String table : tables)
			sqlIn = sqlIn + "'" + table + "',";

		sqlIn = sqlIn.substring(0, sqlIn.lastIndexOf(","));
		String sql = "SHOW TABLE STATUS FROM " + database + " WHERE name IN (" + sqlIn + ")";

		Map<String, TableDesc> map = new HashMap<>();

		try (Statement st = connect.createStatement(); ResultSet result = st.executeQuery(sql);) {
			while (result.next()) {
				String name = result.getString("Name");
				String engine = result.getString("Engine");
				String version = result.getString("Version");
				String row_format = result.getString("Row_format");
				String rows = result.getString("Rows");
				String avg_row_length = result.getString("Avg_row_length");
				String data_length = result.getString("Data_length");
				String max_data_length = result.getString("Max_data_length");
				String index_length = result.getString("Index_length");
				String data_free = result.getString("Data_free");
				String auto_increment = result.getString("Auto_increment");
//				String create_time = result.getString("Create_time");
//				String update_time = result.getString("Update_time");
//				String check_time = result.getString("Check_time");
				String collation = result.getString("Collation");
				String checksum = result.getString("Checksum");
				String create_options = result.getString("Create_options");
				String comment = result.getString("Comment");

				TableDesc tableDesc = new TableDesc();
				tableDesc.setName(name);
				tableDesc.setEngine(engine);
				tableDesc.setVersion(version);
				tableDesc.setRowFormat(row_format);
				tableDesc.setRows(rows);
				tableDesc.setAvgRowLength(avg_row_length);
				tableDesc.setDataLength(data_length);
				tableDesc.setMaxDataLength(max_data_length);
				tableDesc.setIndexLength(index_length);
				tableDesc.setDataFree(data_free);
				tableDesc.setAutoIncrement(auto_increment);
				tableDesc.setCollation(collation);
				tableDesc.setChecksum(checksum);
				tableDesc.setCreateOptions(create_options);
				tableDesc.setComment(comment);
				map.put(name, tableDesc);
			}

			String sql2 = "select table_name, (data_length/1024/1024) AS data_mb, (index_length/1024/1024) AS index_mb,"
					+ " ((data_length+index_length)/1024/1024) AS all_mb, table_rows from information_schema.tables " + "WHERE table_schema = '" + database + "'";

			try (ResultSet rs2 = st.executeQuery(sql2);) {
				while (rs2.next()) {
					String table_name = rs2.getString("TABLE_NAME");
					String data_mb = rs2.getString("data_mb");
					String index_mb = rs2.getString("index_mb");
					String all_mb = rs2.getString("all_mb");
					String table_rows = rs2.getString("TABLE_ROWS");

					TableDesc tableDesc = map.get(table_name);
					tableDesc.setDataMb(data_mb);
					tableDesc.setIndexMb(index_mb);
					tableDesc.setAllMb(all_mb);
					tableDesc.setCount(table_rows);
				}
			}

		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return map;
	}

	/**
	 * 获取表的详情信息
	 * 
	 * @param connect   数据库连接对象
	 * @param database  数据库名
	 * @param tableName 表名
	 * @return
	 */
	public static List<TableColumns> getTableColumns(Connection connect, String database, String tableName) {
		String sql = "SHOW FULL COLUMNS FROM " + database + "." + tableName;

		return getResult(connect, sql, rs -> {
			try {
				while (rs.next()) {
					String field = rs.getString("Field");
					String type = rs.getString("Type");
					String collation = rs.getString("Collation");
					String nullValue = rs.getString("Null");
					String key = rs.getString("Key");
					String defaultValue = rs.getString("Default");
					String extra = rs.getString("Extra");
					String privileges = rs.getString("Privileges");
					String comment = rs.getString("Comment");

					TableColumns columns = new TableColumns();
					columns.setField(field);
					columns.setType(type);
					columns.setCollation(collation);
					columns.setNu(nullValue);
					columns.setKey(key);
					columns.setDefau(defaultValue);
					columns.setExtra(extra);
					columns.setPrivileges(privileges);
					columns.setComment(comment);

					return columns;
				}
			} catch (SQLException e) {
				LOGGER.warning(e);
			}

			return null;
		}, TableColumns.class);
	}

	/**
	 * 获取表索引信息
	 *
	 * @param connect 数据库连接对象
	 * @param sql     要执行的 SQL 语句
	 * @return
	 */
	public static List<TableIndex> getTableIndex(Connection connect, String sql) {
		return getResult(connect, sql, rs -> {
			try {
				String table = rs.getString("Table");
				while (rs.next()) {
					String nonUnique = rs.getString("Non_unique");
					String keyName = rs.getString("Key_name");
					String seqInIndex = rs.getString("Seq_in_index");
					String columnName = rs.getString("Column_name");
					String collation = rs.getString("Collation");
					String cardinality = rs.getString("Cardinality");
					String subPart = rs.getString("Sub_part");
					String packed = rs.getString("Packed");
					String nullValue = rs.getString("Null");
					String indexType = rs.getString("Index_type");
					String comment = rs.getString("Comment");
					String indexComment = rs.getString("Index_comment");
					String visible = rs.getString("Visible");
					String expression = rs.getString("Expression");

					TableIndex index = new TableIndex();
					index.setTable(table);
					index.setNonUnique(nonUnique);
					index.setKeyName(keyName);
					index.setSeqInIndex(seqInIndex);
					index.setColumnName(columnName);
					index.setCollation(collation);
					index.setCardinality(cardinality);
					index.setSubPart(subPart);
					index.setPacked(packed);
					index.setNullValue(nullValue);
					index.setComment(comment);
					index.setIndexType(indexType);
					index.setIndexComment(indexComment);
					index.setVisible(visible);
					index.setExpression(expression);

					return index;
				}
			} catch (SQLException e) {
				LOGGER.warning(e);
			}

			return null;
		}, TableIndex.class);
	}
}
