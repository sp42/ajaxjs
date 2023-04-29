/**
 * Copyright sp42 frank@ajaxjs.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.sql;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.ajaxjs.sql.Lambda.HasZeroResult;
import com.ajaxjs.sql.Lambda.ResultSetProcessor;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 完成 SQL 到 Java 的转换
 *
 * @author sp42 frank@ajaxjs.com
 */
public class JdbcReader {
    private static final LogHelper LOGGER = LogHelper.getLog(JdbcReader.class);

    /**
     * Statement 工厂
     *
     * @param conn   数据库连接对象
     * @param handle 控制器
     */
    public static void stmt(Connection conn, Consumer<Statement> handle) {
        try (Statement stmt = conn.createStatement()) {
            handle.accept(stmt);
        } catch (SQLException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * ResultSet 处理器
     *
     * @param stmt   Statement 对象
     * @param sql    SQL 语句
     * @param handle 控制器
     */
    public static void rsHandle(Statement stmt, String sql, Consumer<ResultSet> handle) {
        LOGGER.info(sql);
        try (ResultSet rs = stmt.executeQuery(sql)) {
            handle.accept(rs);
        } catch (SQLException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * stmt + rsHandle
     *
     * @param conn   数据库连接对象
     * @param sql    SQL 语句
     * @param handle 控制器
     */
    public static void query(Connection conn, String sql, Consumer<ResultSet> handle) {
        stmt(conn, stmt -> rsHandle(stmt, sql, handle));
    }

    /**
     * 执行查询
     *
     * @param <T>
     * @param conn         数据库连接对象 数据库连接对象
     * @param sql          SQL 语句，可以带有 ? 的占位符
     * @param hasZeoResult SQL 查询是否有数据返回，没有返回 true
     * @param processor    如何转换RS到目标结果的处理器
     * @param params       插入到 SQL 中的参数，可单个可多个可不填
     * @return RS 转换后的目标结果
     */
    public static <T> T select(Connection conn, String sql, HasZeroResult hasZeoResult, ResultSetProcessor<T> processor, Object... params) {
        LOGGER.infoYellow("执行 SQL-->[" + JdbcUtil.printRealSql(sql, params) + "]");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (params != null && params.length > 0) {
                // LogHelper.p(params);
                int i = 0;
                for (Object param : params)
                    ps.setObject(++i, param);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (hasZeoResult != null && !hasZeoResult.test(conn, rs, sql))
                    return null;

                return processor.process(rs);
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    /**
     * 查询单行记录(单个结果)，保存为Map&lt;String, Object&gt; 结构。如果查询不到任何数据返回 null。
     *
     * @param conn   数据库连接对象
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return Map&lt;String, Object&gt; 结构的结果。如果查询不到任何数据返回 null。
     */
    public static Map<String, Object> queryAsMap(Connection conn, String sql, Object... params) {
        return select(conn, sql, JdbcHelper::hasZeoResult, JdbcHelper::getResultMap, params);
    }

    /**
     * 查询单行记录(单个结果)，保存为 Bean。如果查询不到任何数据返回 null。
     *
     * @param <T>        实体类型
     * @param beanClz    Bean 实体的类
     * @param connection 数据库连接对象
     * @param sql        SQL 语句，可以带有 ? 的占位符
     * @param params     插入到 SQL 中的参数，可单个可多个可不填
     * @return 查询结果。如果查询不到任何数据返回 null。
     */
    public static <T> T queryAsBean(Class<T> beanClz, Connection connection, String sql, Object... params) {
        return select(connection, sql, JdbcHelper::hasZeoResult, getResultBean(beanClz), params);
    }

    /**
     * 记录集合转换为 Map
     *
     * @param rs 记录集合
     * @return Map 结果
     * @throws SQLException 转换时的 SQL 异常
     */
    public static Map<String, Object> getResultMap(ResultSet rs) throws SQLException {
        // LinkedHashMap 是 HashMap 的一个子类，保存了记录的插入顺序
        Map<String, Object> map = new LinkedHashMap<>();
        ResultSetMetaData rsmd = rs.getMetaData();

        for (int i = 1; i <= rsmd.getColumnCount(); i++) {// 遍历结果集
            String key = JdbcUtil.changeColumnToFieldName(rsmd.getColumnLabel(i));
            Object value = rs.getObject(i);
            map.put(key, value);
        }

        return map;
    }


    /**
     * 记录集合转换为 bean
     *
     * @param beanClz 实体类
     * @return ResultSet 处理器，传入 ResultSet 类型对象返回 T 类型的 bean
     */
    @SuppressWarnings({"unchecked"})
    public static <T> ResultSetProcessor<T> getResultBean(Class<T> beanClz) {
        return rs -> {
            T bean = ReflectUtil.newInstance(beanClz);
            ResultSetMetaData rsmd = rs.getMetaData();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {// 遍历结果集
                String key = rsmd.getColumnLabel(i);
                Object _value = rs.getObject(i); // Real value in DB

                if (key.contains("_")) // 将以下划线分隔的数据库字段转换为驼峰风格的字符串
                    key = JdbcUtil.changeColumnToFieldName(key);

                try {
                    PropertyDescriptor property = new PropertyDescriptor(key, beanClz);
                    Method method = property.getWriteMethod();
                    Object value = null;

                    Class<?> propertyType = property.getPropertyType();

                    // 枚举类型的支持
//					if (propertyType.isEnum()) // Enum.class.isAssignableFrom(propertyType) 这个方法也可以
//						value = dbValue2Enum(propertyType, _value);
//					else {
                    try {
                        value = MappingValue.objectCast(_value, propertyType);
                    } catch (NumberFormatException e) {
                        String input = (value == null ? " 空值 " : value.getClass().toString()), expect = property.getPropertyType().toString();
                        LOGGER.warning(e, "保存数据到 bean 的 {0} 字段时，转换失败，输入值：{1}，输入类型 ：{2}， 期待类型：{3}", key, value, input, expect);
                        continue; // 转换失败，继续下一个字段
                    }
//					}

                    ReflectUtil.executeMethod(bean, method, value);
                } catch (IntrospectionException | IllegalArgumentException e) {
                    if (e instanceof IntrospectionException) {
                        // 数据库返回这个字段，但是 bean 没有对应的方法
//						LOGGER.info("数据库返回这个字段 {0}，但是 bean {1} 没有对应的方法", key, beanClz);
//						e.printStackTrace();

                        try {
                            if ((_value != null) && beanClz.getField("extractData") != null) {
                                Object obj = ReflectUtil.executeMethod(bean, "getExtractData");

//								LOGGER.info(":::::::::key::"+ key +":::v:::" + _value);
                                if (obj == null) {
                                    Map<String, Object> extractData = new HashMap<>();
                                    ReflectUtil.executeMethod(bean, "setExtractData", extractData);
                                    obj = ReflectUtil.executeMethod(bean, "getExtractData");
                                }

                                Map<String, Object> map = (Map<String, Object>) obj;
                                map.put(key, _value);
                            }
                        } catch (NoSuchFieldException | SecurityException e1) {
//							 LOGGER.warning(e);
                        }

                        continue;
                    }

                    LOGGER.warning(e);
                }
            }

            return bean;
        };
    }

    /**
     * 数据库中的值转换为 Java 枚举
     *
     * @param propertyType
     * @param _value
     * @return
     */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	private static Object dbValue2Enum(Class<?> propertyType, Object _value) {
//		if (_value != null) {
//			boolean isNumber = _value instanceof Number, isString = _value instanceof String; // 写在这里不用在 for 里面每次判断
//
//			for (Object e : propertyType.getEnumConstants()) {
//				if (isNumber) {
//					IntegerValueEnum _e = (IntegerValueEnum) e;
//					Integer _int = ((Integer) _value) - (_e.isUsingOrdinal() ? 1 : 0);
//
//					if (_int == _e.getValue()) {
//						return e;
//					}
//				} else if (isString)
//					return Enum.valueOf((Class<Enum>) propertyType, _value + "");
//			}
//		}
//
//		return null;
//	}

    /**
     * 查询列表
     *
     * @param conn   数据库连接对象
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return 查询列表结果
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> queryAsList(Connection conn, String sql, Object... params) {
        return select(conn, sql, null, rs -> forEachRs(rs, _rs -> (T) rs.getObject(0)), params);
    }

    /**
     * 查询一组结果，保存为 List&lt;Map&lt;String, Object&gt;&gt; 结构。如果查询不到任何数据返回 null。
     *
     * @param conn   数据库连接对象
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return List&lt;Map&lt;String, Object&gt;&gt; 结构的结果。如果查询不到任何数据返回 null。
     */
    public static List<Map<String, Object>> queryAsMapList(Connection conn, String sql, Object... params) {
        return select(conn, sql, null, rs -> forEachRs(rs, JdbcHelper::getResultMap), params);
    }

    /**
     * 查询一组结果，保存为 List&lt;Bean&gt; 结构。如果查询不到任何数据返回 null。
     *
     * @param beanClz Bean 实体的类
     * @param conn    数据库连接对象
     * @param sql     SQL 语句，可以带有 ? 的占位符
     * @param params  插入到 SQL 中的参数，可单个可多个可不填
     * @return List&lt;Bean&gt; 结构的结果。如果查询不到任何数据返回 null。
     */
    public static <T> List<T> queryAsBeanList(Class<T> beanClz, Connection conn, String sql, Object... params) {
        return select(conn, sql, null, rs -> forEachRs(rs, getResultBean(beanClz)), params);
    }

    /**
     * ResultSet 迭代器
     *
     * @param rs        结果集合
     * @param processor 单行处理器
     * @return 多行记录列表集合
     * @throws SQLException
     */
    static <T> List<T> forEachRs(ResultSet rs, ResultSetProcessor<T> processor) throws SQLException {
        List<T> list = new ArrayList<>();

        while (rs.next()) {
            T d = processor.process(rs);
            list.add(d);
        }

        return list.size() > 0 ? list : null; // 找不到记录返回 null，不返回空的 list
    }

    /**
     * 判断 ResultSet 是否有匹配的查询数据
     *
     * @param conn 数据库连接对象
     * @param rs   结果集合
     * @param sql  SQL 语句，可以带有 ? 的占位符
     * @return true 表示有数据
     * @throws SQLException
     */
    static boolean hasZeoResult(Connection conn, ResultSet rs, String sql) throws SQLException {
        boolean hasNext;

//		if (JdbcConnection.getDaoContext().getDbType() == DataBaseType.SQLITE)
//			hasNext = rs.isBeforeFirst(); // SQLite 比较特殊，要用 isBeforeFirst() 方法判断
//		else
        hasNext = rs.next();

        if (hasNext)
            return true;
        else {
            LOGGER.info("查询 SQL：{0} 没有符合的记录！", sql);
            return false;
        }
    }

    /**
     * 有且只有一行记录，并只返回第一列的字段。可指定字段的数据类型
     *
     * @param conn   数据库连接对象
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param clz    期望的结果类型
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return 数据库里面的值作为 T 出现
     */
    @SuppressWarnings("unchecked")
    public static <T> T queryOne(Connection conn, String sql, Class<T> clz, Object... params) {
        Map<String, Object> map = queryAsMap(conn, sql, params);

        if (map != null)
            for (String key : map.keySet()) {// 有且只有一个记录
                Object obj = map.get(key);

                if (obj == null)
                    return null;
                else {
                    if (obj instanceof Long && clz == int.class) {
                        Object _int = ((Long) obj).intValue();
                        return (T) _int;
                    }

                    if (obj instanceof Integer && (clz == long.class || clz == Long.class)) {
                        Object _int = ((Integer) obj).longValue();
                        return (T) _int;
                    }

                    return (T) obj;
                }
            }

        return null;
    }

    /**
     * 查询数组
     *
     * @param conn   数据库连接对象
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param clz    注意 Integer.class 不能用 int.class 代替
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return 数组结构的结果集合
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] queryArray(Connection conn, String sql, Class<T> clz, Object... params) {
        return select(conn, sql, null, (ResultSet rs) -> {
            List<T> list = forEachRs(rs, _rs -> (T) rs.getObject(1));

            if (list == null)
                return null;

            Object array = Array.newInstance(clz, list.size());// List 转为数组

            for (int i = 0; i < list.size(); i++)
                Array.set(array, i, list.get(i));

            return (T[]) array;
        }, params);
    }
}
