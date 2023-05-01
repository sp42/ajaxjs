package com.ajaxjs.data.jdbc_helper.common;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 如何转换 ResultSet 到目标结果的处理器
 *
 * @param <T> 结果的类型
 */
@FunctionalInterface
public interface ResultSetProcessor<T> {
    /**
     * 转换结果
     *
     * @param rs JDBC 结果集合
     * @throws SQLException SQL 异常
     */
    T process(ResultSet rs) throws SQLException;
}