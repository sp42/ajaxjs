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
        String sql = "SELECT table_schema, CONCAT(TRUNCATE(SUM(max_data_length)/1024/1024,2),'mb') AS max_data_size, " +
                "SUM( data_length + index_length ) / 1024 / 1024 AS total_mb, SUM( data_length ) / 1024 / 1024 AS data_mb," +
                "CONCAT(TRUNCATE(SUM(data_free)/1024/1024,2),'mb') AS data_free, SUM( index_length ) / 1024 / 1024 AS index_mb," +
                "COUNT(*) AS TABLES, CURDATE() AS today FROM information_schema.TABLES WHERE table_schema = '" + database +
                "' GROUP BY table_schema ORDER BY 2 DESC";

        return getMapResult(sql, (rs, map) -> {
            try {
                ResultSetMetaData meta = rs.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    if (i == 1)
                        rs.next();

                    String name = meta.getColumnName(i), value = rs.getString(name);
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
    public Map<String, TableDesc> getTableDesc(String database, List<String> tables) {
        StringBuilder sqlIn = new StringBuilder();

        for (String table : tables)
            sqlIn.append("'").append(table).append("',");

        sqlIn = new StringBuilder(sqlIn.substring(0, sqlIn.lastIndexOf(",")));
        String sql = "SHOW TABLE STATUS FROM " + database + " WHERE name IN (" + sqlIn + ")";
        Map<String, TableDesc> map = new HashMap<>();

        try (Statement st = conn.createStatement(); ResultSet result = st.executeQuery(sql)) {
            while (result.next()) {
//				String create_time = result.getString("Create_time");
//				String update_time = result.getString("Update_time");
//				String check_time = result.getString("Check_time");

                TableDesc tableDesc = new TableDesc();
                tableDesc.setName(result.getString("Name"));
                tableDesc.setEngine(result.getString("Engine"));
                tableDesc.setVersion(result.getString("Version"));
                tableDesc.setRowFormat(result.getString("Row_format"));
                tableDesc.setRows(result.getString("Rows"));
                tableDesc.setAvgRowLength(result.getString("Avg_row_length"));
                tableDesc.setDataLength(result.getString("Data_length"));
                tableDesc.setMaxDataLength(result.getString("Max_data_length"));
                tableDesc.setIndexLength(result.getString("Index_length"));
                tableDesc.setDataFree(result.getString("Data_free"));
                tableDesc.setAutoIncrement(result.getString("Auto_increment"));
                tableDesc.setCollation(result.getString("Collation"));
                tableDesc.setChecksum(result.getString("Checksum"));
                tableDesc.setCreateOptions(result.getString("Create_options"));
                tableDesc.setComment(result.getString("Comment"));

                map.put(tableDesc.getName(), tableDesc);
            }

            String sql2 = "SELECT table_name, (data_length/1024/1024) AS data_mb, (index_length/1024/1024) AS index_mb,"
                    + " ((data_length+index_length)/1024/1024) AS all_mb, table_rows from information_schema.tables " +
                    "WHERE table_schema = '" + database + "'";

            try (ResultSet rs2 = st.executeQuery(sql2)) {
                while (rs2.next()) {
                    TableDesc tableDesc = map.get(rs2.getString("TABLE_NAME"));

                    if (tableDesc != null) {
                        tableDesc.setDataMb(rs2.getString("data_mb"));
                        tableDesc.setIndexMb(rs2.getString("index_mb"));
                        tableDesc.setAllMb(rs2.getString("all_mb"));
                        tableDesc.setCount(rs2.getString("TABLE_ROWS"));
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
     * @param database  数据库名
     * @param tableName 表名
     * @return 表的详情信息
     */
    public List<TableColumns> getTableColumns(String database, String tableName) {
        String sql = "SHOW FULL COLUMNS FROM " + database + "." + tableName;

        return getResult(sql, rs -> {
            TableColumns columns = new TableColumns();

            try {
                columns.setField(rs.getString("Field"));
                columns.setType(rs.getString("Type"));
                columns.setCollation(rs.getString("Collation"));
                columns.setNu(rs.getString("Null"));
                columns.setKey(rs.getString("Key"));
                columns.setDefau(rs.getString("Default"));
                columns.setExtra(rs.getString("Extra"));
                columns.setPrivileges(rs.getString("Privileges"));
                columns.setComment(rs.getString("Comment"));

                return columns;
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
                TableIndex index = new TableIndex();
                index.setTable(rs.getString("Table"));
                index.setNonUnique(rs.getString("Non_unique"));
                index.setKeyName(rs.getString("Key_name"));
                index.setSeqInIndex(rs.getString("Seq_in_index"));
                index.setColumnName(rs.getString("Column_name"));
                index.setCollation(rs.getString("Collation"));
                index.setCardinality(rs.getString("Cardinality"));
                index.setSubPart(rs.getString("Sub_part"));
                index.setPacked(rs.getString("Packed"));
                index.setNullValue(rs.getString("Null"));
                index.setComment(rs.getString("Comment"));
                index.setIndexType(rs.getString("Index_type"));
                index.setIndexComment(rs.getString("Index_comment"));
                index.setVisible(rs.getString("Visible"));
                index.setExpression(rs.getString("Expression"));

                return index;
            } catch (SQLException e) {
                LOGGER.warning(e);
            }

            return null;
        }, TableIndex.class);
    }
}
