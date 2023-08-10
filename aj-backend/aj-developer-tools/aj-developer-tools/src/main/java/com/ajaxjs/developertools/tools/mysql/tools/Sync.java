package com.ajaxjs.developertools.tools.mysql.tools;

import com.ajaxjs.util.logger.LogHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跨服务器实现同步表结构，数据
 * <p>
 * 不同服务器之间的数据库中的表结构，数据进行同步操作，测试类，可直接运行，部分不同类型库之间的转换，还未完全完善，可能存在字段类型转换失败，导致建表失败，同步数据支持百万千万的数据量
 *
 * @author <a href="http://www.yanzuoguang.com/article/835.html">...</a>
 */
public class Sync {
    private static final LogHelper LOGGER = LogHelper.getLog(Sync.class);

    private Connection conn;

    private Connection connR;

    public static final String typeR = "oracle";

    public void run() {
//        List<Map<String, Object>> list = new ArrayList<>();
//        StringBuffer sql = new StringBuffer();

        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            ResultSet tables = databaseMetaData.getTables(null, null, "%", null);

            while (tables.next()) new Sync().sync(tables.getString("TABLE_NAME"));
        } catch (Exception e) {
            LOGGER.warning(e);
        }

    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> sync(String tableName) throws Exception {
        Map<String, Object> map = new HashMap<>();
//		long start = System.currentTimeMillis();
        // 生成建表sql
        Map<String, Object> sql = sql(tableName);
//		Map<String, Object> columnMap = (Map<String, Object>) sql.get("map");
        Map<String, Object> sqlMap = (Map<String, Object>) sql.get("sql");
        String createTableSQL = (String) sqlMap.get("sql");

        System.out.println("-------获取表结构成功，建表sql生成成功-----");
        createTable(createTableSQL, tableName);

        return map;
    }

    /**
     * 获取建表 SQL
     */
    public Map<String, Object> sql(String tableName) {
        Map<String, Object> map = new HashMap<>(), sqlMap = new HashMap<>();
        String sql;
        List<String> primaryKeyList = new ArrayList<>();

        try {
            String catalog = conn.getCatalog(); // catalog 是数据库名
            System.out.println("---------连接成功，数据库：" + catalog);
            DatabaseMetaData metaData = conn.getMetaData();

            // 获取表
            try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
                Map<String, Object> dataMap = new HashMap<>();
                List<HashMap<String, Object>> rows = new ArrayList<>();

                // 获取信息
                while (rs.next()) {
                    HashMap<String, Object> row = new HashMap<>();
                    dataMap.put("TABLE_NAME", tableName);
                    row.put("COLUMN_NAME", rs.getString("COLUMN_NAME")); // 字段名
                    row.put("TYPE_NAME", rs.getString("TYPE_NAME")); // 字段类型

                    if ("DATETIME".equals(rs.getString("TYPE_NAME")))
                        row.put("COLUMN_SIZE", 0); // 如果事 dataTime 类型修改为 0，调试返回时 19，创表失败
                    else row.put("COLUMN_SIZE", rs.getInt("COLUMN_SIZE"));

                    map.put(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"));
                    row.put("NULLABLE", rs.getInt("NULLABLE") == 0 ? "NOT NULL" : " "); // 可否为 null
                    rows.add(row);
                }

                try (ResultSet pt = metaData.getPrimaryKeys(conn.getCatalog(), null, tableName)) {// 主键
                    while (pt.next()) primaryKeyList.add(pt.getString("COLUMN_NAME"));
                }

                dataMap.put("PRIMARYS", primaryKeyList); // 获取主键
                dataMap.put("rows", rows);

                sql = getSql(dataMap, tableName, typeR);
                sqlMap.put("sql", sql);

                Map<String, Object> all = new HashMap<>();
                all.put("map", map);
                all.put("sql", sqlMap);

                return all;
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    /**
     * 拼接建表 sql
     */
    @SuppressWarnings("unchecked")
    public String getSql(Map<String, Object> map, String tableName, String typeR) {
        StringBuilder sb = new StringBuilder();
        List<String> PRIMARYS = (List<String>) map.get("PRIMARYS");

        sb.append("CREATE TABLE ").append(map.get("TABLE_NAME")).append(" (").append("\n");
        List<HashMap<String, Object>> rows = (List<HashMap<String, Object>>) map.get("rows");

        for (Map<String, Object> rowMap : rows) {
            if ("mysql".equals(typeR)) sb.append("`").append(rowMap.get("COLUMN_NAME")).append("` ");
            else sb.append("   ").append(rowMap.get("COLUMN_NAME")).append("  ");

            sb.append(caseVale(rowMap.get("TYPE_NAME"), typeR));

            // sql server 除了varchar类型，其他都加大小
            if ("sql_server".equals(typeR)) {
                if ((caseVale(rowMap.get("TYPE_NAME"), typeR)).equals("VARCHAR"))
                    sb.append("(").append(rowMap.get("COLUMN_SIZE")).append(")");

                // sql server 不存在float和double,转换成decimal
                if ((caseVale(rowMap.get("TYPE_NAME"), typeR)).equals("DECIMAL")) sb.append("(12,4)");
            }

            // mysql , dateTime和时间戳类型不需要大小
            else if ("mysql".equals(typeR)) {
                if (caseVale(rowMap.get("TYPE_NAME"), typeR).equals("DATETIME") || caseVale(rowMap.get("TYPE_NAME"), typeR).equals("TIMESTAMP(6)") || caseVale(rowMap.get("TYPE_NAME"), typeR).equals("LONGTEXT") || caseVale(rowMap.get("TYPE_NAME"), typeR).equals("TEXT")) {
                    // sb.append("(0)");
                } else if (caseVale(rowMap.get("TYPE_NAME"), typeR).equals("CHAR(1)")) {
                } else if (caseVale(rowMap.get("TYPE_NAME"), typeR).equals("DOUBLE") || caseVale(rowMap.get("TYPE_NAME"), typeR).equals("FLOAT")) {
                    sb.append("(12,4)");
                } else if (caseVale(rowMap.get("TYPE_NAME"), typeR).equals("BIT")) {
                    sb.append("(1)");
                } else {
                    sb.append("(").append(rowMap.get("COLUMN_SIZE")).append(")");
                }
            } else {
                // 如果是oracle的date类型，不需要加大小
                if (!caseVale(rowMap.get("TYPE_NAME"), typeR).equals("DATE") && !caseVale(rowMap.get("TYPE_NAME"), typeR).equals("DOUBLE") && !caseVale(rowMap.get("TYPE_NAME"), typeR).equals("FLOAT")) {
                    if (caseVale(rowMap.get("TYPE_NAME"), typeR).equals("DOUBLE")) {
                        sb.append("(12,4)");
                    } else if (caseVale(rowMap.get("TYPE_NAME"), typeR).equals("CHAR")) {
                        sb.append("(1)");
                    } else if (caseVale(rowMap.get("TYPE_NAME"), typeR).equals("VARCHAR2")) {
                        sb.append("(255)");
                    } else {
                        sb.append("(").append(rowMap.get("COLUMN_SIZE")).append(")");
                    }
                }
            }

            sb.append(rowMap.get("NULLABLE") == "" ? "" : " " + rowMap.get("NULLABLE")).append(",");
            sb.append("\n");
        }

        if (PRIMARYS.size() > 0) sb.append("  PRIMARY KEY (");

        for (String primary : PRIMARYS) sb.append(primary).append(",");

        sb.deleteCharAt(sb.lastIndexOf(","));
        if (PRIMARYS.size() > 0) sb.append(")\n");

        sb.append(")");
        System.out.println("-----------建表语句\n" + sb);

        return sb.toString();
    }

    /**
     * 两方不同数据库，需要对类型进行转换
     */
    public String caseVale(Object typeName, String type) {
        String typeClound;

        if ("mysql".equals(type)) {
            switch (((String) typeName).toUpperCase()) {
                case "NUMBER":
                    typeClound = "BIGINT";
                    break;
                case "DATE":
                case "TIMESTAMP(6)":
                case "TIMESTAMP":
                    typeClound = "DATETIME";
                    break;
                case "VARCHAR2":
                    typeClound = "VARCHAR";
                    break;

                default:
                    typeClound = ((String) typeName).toUpperCase();
                    break;
            }
        } else if ("oracle".equals(type)) {
            switch (((String) typeName).toUpperCase()) {
                case "BIT":
                    typeClound = "CHAR";
                    break;
                case "DATETIME":
                case "TIMESTAMP(6)":
                case "TIMESTAMP":
                    typeClound = "DATE";
                    break;
                case "INT":
                case "SMALLINT":
                case "BIGINT":
                case "FLOAT":
                case "DOUBLE":
                    typeClound = "NUMBER";
                    break;
                case "TEXT":
                    typeClound = "VARCHAR2";
                    break;
                default:
                    typeClound = ((String) typeName).toUpperCase();
                    break;
            }
        } else {
            switch (((String) typeName).toUpperCase()) {
                case "NUMBER":
                    typeClound = "BIGINT";
                    break;
                case "DATE":
                case "TIMESTAMP(6)":
                    typeClound = "DATETIME";
                    break;
                case "CHAR":
                    typeClound = "BIT";
                    break;
                case "VARCHAR2":
                    typeClound = "VARCHAR";
                    break;
                case "FLOAT":
                case "DOUBLE":
                    typeClound = "DECIMAL";
                    break;
                case "LONGTEXT":
                    typeClound = "TEXT";
                    break;
                case "BLOB":
                    typeClound = "IMAGE";
                    break;
                default:
                    typeClound = ((String) typeName).toUpperCase();
                    break;
            }
        }

        return typeClound;
    }

    /**
     * 连接对方系统进行建表
     */
    public boolean createTable(String sql, String tableName) throws Exception {
        if (typeR.equals("oracle")) {
            try (PreparedStatement preparedStatement = connR.prepareStatement(sql)) {
                preparedStatement.execute();
                return exitsTable(connR, tableName);
            }
        } else return exitsTable(connR, tableName);
    }

    /**
     * 判断对方表是否存在
     */
    public static boolean exitsTable(Connection conn, String tableName) {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName.toUpperCase(), null)) {
            return rs.next();
        } catch (SQLException e) {
            LOGGER.warning(e);
        }

        return false;
    }

    /**
     * 获取 insert 模板
     */
    public Map<String, Object> sqlTemplet(List<Map<String, Object>> list, String tableName) {
        List<String> fieldList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(tableName).append(" (");

        for (Map.Entry<String, Object> entry : list.get(0).entrySet()) {
            sb.append(entry.getKey()).append(",");
            fieldList.add(entry.getKey());
        }

        sb.deleteCharAt(sb.lastIndexOf(",")).append(") values (");
        for (@SuppressWarnings("unused") Map.Entry<String, Object> entry : list.get(0).entrySet())
            sb.append("?").append(",");

        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")\n");
        System.out.println(sb);
        map.put("fieldList", fieldList);
        map.put("sqlTemplet", sb.toString());

        return map;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnR() {
        return connR;
    }

    public void setConnR(Connection connR) {
        this.connR = connR;
    }
}
