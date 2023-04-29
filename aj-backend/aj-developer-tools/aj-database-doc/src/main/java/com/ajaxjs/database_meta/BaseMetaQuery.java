package com.ajaxjs.database_meta;

import com.ajaxjs.util.logger.LogHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 抽象基类，用于数据库元数据查询
 */
public abstract class BaseMetaQuery {
    private static final LogHelper LOGGER = LogHelper.getLog(BaseMetaQuery.class);

    Connection conn;

    public BaseMetaQuery(Connection conn) {
        this.conn = conn;
    }

    /**
     * 获取查询结果，返回 List 类型的数据集合
     *
     * @param <T> 范型，要返回的 List 中元素的类别
     * @param sql 要执行的 SQL 语句
     * @param cb  对于每个查询结果行，执行回调函数，将 ResultSet 转成 Java 对象 T
     * @param clz Java 对象的类别
     * @return 返回转换后的 Java 对象 T 所组成的 List
     */
    public <T> List<T> getResult(String sql, Function<ResultSet, T> cb, Class<T> clz) {
        List<T> list = new ArrayList<>();

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                T v = cb.apply(rs);
                list.add(v);
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
        }

        return list;
    }

    /**
     * 获取查询结果，返回 Map 类型的数据集合
     *
     * @param sql    要执行的 SQL 语句
     * @param cb     对于每个查询结果行，执行回调函数，将 ResultSet 转成 Map<String, String>
     *               <p>
     *               注意：Map 的 key 是列名（column name），value 是列值（column value）
     * @param isLoop 是否需要循环处理 ResultSet 中的每一行
     * @return 返回转换后的 Map<String, String> 所组成的 Map 对象，可能包含多行数据
     */
    public Map<String, String> getMapResult(String sql, BiConsumer<ResultSet, Map<String, String>> cb, boolean isLoop) {
        Map<String, String> map = new HashMap<>();

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (isLoop) {
                while (rs.next())
                    cb.accept(rs, map);
            } else
                cb.accept(rs, map);
        } catch (SQLException e) {
            LOGGER.warning(e);
        }

        return map;
    }

    /**
     * 获取查询结果，返回 Map 类型的数据集合。
     * <p>
     * 默认为需要循环处理 ResultSet 中的每一行
     *
     * @param sql 要执行的 SQL 语句
     * @param cb  对于每个查询结果行，执行回调函数，将 ResultSet 转成 Map<String, String>
     *            <p>
     *            注意：Map 的 key 是列名（column name），value 是列值（column value）
     * @return 返回转换后的 Map<String, String> 所组成的 Map 对象，可能包含多行数据
     */
    public Map<String, String> getMapResult(String sql, BiConsumer<ResultSet, Map<String, String>> cb) {
        return getMapResult(sql, cb, true);
    }
}
