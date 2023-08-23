package com.ajaxjs.data.util;

import com.ajaxjs.sql.JdbcReader;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取数据库元数据信息。当前只支持 MySQL
 *
 * @author Frank Cheung
 */
public class DataBaseMetaHelper {
    private static final LogHelper LOGGER = LogHelper.getLog(DataBaseMetaHelper.class);

    /**
     * 获得某表的注释
     *
     * @param conn      数据库连接对象。注意这个方法并不会关闭数据库连接
     * @param tableName 表名
     * @return 表注释
     */
    public static String getTableComment(Connection conn, String tableName) {
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName)) {
            if (rs != null && rs.next()) {
                String createDDL = rs.getString(2);

                return parse(createDDL);
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    /**
     * 获得多张表的注释
     *
     * @param conn       数据库连接对象。注意这个方法并不会关闭数据库连接。
     * @param tableNames 表名集合
     * @param dbName     数据库名，可选的
     * @return 表注释集合，key 是表名，value 是注释
     */
    public static Map<String, String> getTableComment(Connection conn, List<String> tableNames, String dbName) {
        Map<String, String> map = new HashMap<>();
        boolean hasDbName = StringUtils.hasText(dbName);

        try (Statement stmt = conn.createStatement()) {
            for (String tableName : tableNames) {
                String t = hasDbName ? dbName + "." + tableName : tableName;

                try (ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + t)) {
                    String createDDL = null;

                    try {
                        if (rs.next())
                            createDDL = rs.getString(2);
                    } catch (SQLException e) {
                        LOGGER.warning(e);
                    }

                    String comment = parse(createDDL);
                    map.put(tableName, comment);
                }
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
        }

        return map;
    }

    /**
     * 获得多张表的注释，返回的 Map 带有 key 注解的，并保存到 List 中
     *
     * @param conn       数据库连接对象。注意这个方法并不会关闭数据库连接。
     * @param tableNames 表名集合
     * @param dbName     数据库名，可选的
     * @return 表注释集合，固定 key，分别是 tableName、comment
     */
    public static List<Map<String, Object>> getTableCommentWithAnnotateAsList(Connection conn, List<String> tableNames, String dbName) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> tableComment = getTableComment(conn, tableNames, dbName);

        for (String tableName : tableComment.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", tableName);
            map.put("comment", tableComment.get(tableName));

            list.add(map);
        }

        return list;
    }

    /**
     * 返回注释信息
     *
     * @param all DDL
     * @return 注释信息
     */
    public static String parse(String all) {
        if (all == null)
            return null;

        int index = all.indexOf("COMMENT='");
        if (index < 0)
            return "";

        String comment = all.substring(index + 9);

        return comment.substring(0, comment.length() - 1);
    }

    /**
     * 获取当前数据库下的所有表名称
     *
     * @param conn   数据库连接对象
     * @param dbName 数据库名，可选的
     * @return 所有表名称
     */
    public static List<String> getAllTableName(Connection conn, String dbName) {
        List<String> tables = new ArrayList<>();
        String sql = StringUtils.hasText(dbName) ? "SHOW TABLES FROM " + dbName : "SHOW TABLES";

        JdbcReader.query(conn, sql, rs -> {
            try {
                while (rs.next())
                    tables.add(rs.getString(1));
            } catch (SQLException e) {
                LOGGER.warning(e);
            }
        });

        return tables;
    }

    /**
     * 获取多张表的各个字段的注释
     *
     * @param conn       数据库连接对象
     * @param tableNames 多张表的表名
     * @return 包含给个字段注释的 Map，key 是表名，value 是各个列。列中的Map
     */
    public static Map<String, List<Map<String, String>>> getColumnComment(Connection conn, List<String> tableNames) {
        Map<String, List<Map<String, String>>> map = new HashMap<>();

        JdbcReader.stmt(conn, stmt -> {
            for (String tableName : tableNames) {
                JdbcReader.rsHandle(stmt, "SHOW FULL COLUMNS FROM " + tableName, rs -> {
                    List<Map<String, String>> list = new ArrayList<>();
                    rs2list(rs, list);
                    map.put(tableName, list);
                });
            }
        });

        return map;
    }

    /**
     * 获取一张表的各个字段的注释
     *
     * @param conn      数据库连接对象
     * @param tableName 单张表名
     * @param dbName    数据库名，可选的
     * @return 一张表的各个字段的注释
     */
    public static List<Map<String, String>> getColumnComment(Connection conn, String tableName, String dbName) {
        String target = "";

        if (StringUtils.hasText(dbName))
            target += dbName + ".";

        target += tableName;

        List<Map<String, String>> list = new ArrayList<>();
        JdbcReader.query(conn, "SHOW FULL COLUMNS FROM " + target, rs -> rs2list(rs, list));

        return list;
    }

    /**
     * 获取所有库名
     *
     * @param conn 数据库连接对象
     * @return 所有库名
     */
    public static List<String> getDatabase(Connection conn) {
        List<String> list = new ArrayList<>();

        JdbcReader.query(conn, "SHOW DATABASES", rs -> {
            try {
                while (rs.next())
                    list.add(rs.getString("Database"));
            } catch (SQLException e) {
                LOGGER.warning(e);
            }
        });

        return list;
    }

    private static Pattern getLength;

    private static void rs2list(ResultSet rs, List<Map<String, String>> list) {
        if (getLength == null)
            getLength = Pattern.compile("\\((\\d+)\\)");

        try {
            while (rs.next()) {
                Map<String, String> fieldInfo = new HashMap<>();
                fieldInfo.put("name", rs.getString("Field"));
                String type = rs.getString("Type");

                Matcher m = getLength.matcher(type);
                fieldInfo.put("length", (m.find() ? m.group(1) : "null"));
                fieldInfo.put("type", m.replaceAll(""));
                fieldInfo.put("null", rs.getString("Null"));
                fieldInfo.put("comment", rs.getString("Comment"));
                fieldInfo.put("default", rs.getString("Default"));
                String key = rs.getString("Key");
                fieldInfo.put("isKey", String.valueOf(StringUtils.hasText(key) && "PRI".equals(key)));

                list.add(fieldInfo);
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
        }
    }

}
