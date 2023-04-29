package com.ajaxjs.database_meta;

import com.ajaxjs.database_meta.model.TableColumns;
import com.ajaxjs.database_meta.model.TableDesc;
import com.ajaxjs.database_meta.model.TableIndex;
import com.ajaxjs.util.logger.LogHelper;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一些数据库的详情，好像没什么用
 *
 * @author Frank Cheung sp42@qq.com
 */
public class MetaQuery extends BaseMetaQuery {
    private static final LogHelper LOGGER = LogHelper.getLog(MetaQuery.class);

    public MetaQuery(Connection conn) {
        super(conn);
    }

    /**
     * 查询某个变量
     *
     * @param name 变量名名
     * @param sql  要执行的 SQL 语句
     */
    public String getVariable(String name, String sql) {
        try (Statement st = conn.createStatement(); ResultSet result = st.executeQuery(sql)) {
            if (result.next())
                return result.getString(name);
        } catch (SQLException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    /**
     * 获取所有变量
     *
     * @return 所有变量
     */
    public Map<String, String> getAllVariable() {
        return getVariables("SHOW VARIABLES");
    }

    /**
     * 获取所有变量
     *
     * @param sql 要执行的 SQL 语句
     * @return 所有变量
     */
    public Map<String, String> getVariables(String sql) {
        return getMapResult(sql, (rs, map) -> {
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
     * @param database 数据库名
     * @return 数据库的大小
     */
    public Map<String, String> getDbSize(String database) {
        String sql = "SELECT table_schema, concat(truncate(sum(max_data_length)/1024/1024,2),'mb') AS max_data_size, "
                + "sum( data_length + index_length ) / 1024 / 1024 AS total_mb,sum( data_length ) / 1024 / 1024 AS data_mb, "
                + "concat(truncate(sum(data_free)/1024/1024,2),'mb') as data_free, sum( index_length ) / 1024 / 1024 AS index_mb, "
                + "count( * ) AS TABLES,curdate() AS today FROM information_schema.TABLES where table_schema = '" + database
                + "' GROUP BY table_schema ORDER BY 2 DESC ;";

        return getMapResult(sql, (rs, map) -> {
            String name, value;

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
     * @param sql 要执行的 SQL 语句 例如 SHOW TABLES IN xxx
     * @return 表信息
     */
    public List<String> getTables(String sql) {
        return getResult(sql, (rs) -> {
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
     * @param database 数据库名
     * @param tables   表名集合
     * @return 表的详情信息
     */
    public Map<String, TableDesc> getTableDesc(String database, String[] tables) {
        String sqlIn = "";

        for (String table : tables)
            sqlIn = sqlIn + "'" + table + "',";

        sqlIn = sqlIn.substring(0, sqlIn.lastIndexOf(","));
        String sql = "SHOW TABLE STATUS FROM " + database + " WHERE name IN (" + sqlIn + ")";
        Map<String, TableDesc> map = new HashMap<>();

        try (Statement st = conn.createStatement(); ResultSet result = st.executeQuery(sql)) {
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
                    + " ((data_length+index_length)/1024/1024) AS all_mb, table_rows from information_schema.tables " + "WHERE table_schema = '"
                    + database + "'";

            try (ResultSet rs2 = st.executeQuery(sql2)) {
                while (rs2.next()) {
                    String table_name = rs2.getString("TABLE_NAME");
                    String data_mb = rs2.getString("data_mb");
                    String index_mb = rs2.getString("index_mb");
                    String all_mb = rs2.getString("all_mb");
                    String table_rows = rs2.getString("TABLE_ROWS");

                    TableDesc tableDesc = map.get(table_name);
                    if (tableDesc != null) {
                        tableDesc.setDataMb(data_mb);
                        tableDesc.setIndexMb(index_mb);
                        tableDesc.setAllMb(all_mb);
                        tableDesc.setCount(table_rows);
                    }
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
     * @return 表的详情信息
     */
    public List<TableColumns> getTableColumns(Connection connect, String database, String tableName) {
        String sql = "SHOW FULL COLUMNS FROM " + database + "." + tableName;

        return getResult(sql, rs -> {
            try {
                if (rs.next()) {
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
     * @param sql 要执行的 SQL 语句
     * @return 表索引信息
     */
    public List<TableIndex> getTableIndex(String sql) {
        return getResult(sql, rs -> {
            try {
                String table = rs.getString("Table");
                if (rs.next()) {
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
