package com.ajaxjs.data.jdbc_helper;

import com.ajaxjs.data.DataUtils;
import com.ajaxjs.data.jdbc_helper.common.ResultSetProcessor;
import com.ajaxjs.framework.MyConvert;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.Methods;
import com.ajaxjs.util.reflect.NewInstance;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 基本 JDBC 查询操作的封装
 */
public class JdbcReader extends JdbcConn {
    private static final LogHelper LOGGER = LogHelper.getLog(JdbcReader.class);

    /**
     * 执行查询
     *
     * @param <T>       结果的类型
     * @param processor 结果处理器
     * @param sql       SQL 语句
     * @param params    插入到 SQL 中的参数，可单个可多个可不填
     * @return 查询结果，如果为 null 表示没有数据
     */
    public <T> T executeQuery(ResultSetProcessor<T> processor, String sql, Object... params) {
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            LOGGER.infoYellow("执行 SQL-->[" + DataUtils.printRealSql(sql, params) + "]");
            setParam2Ps(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return processor.process(rs);
                } else {
                    LOGGER.info("查询 SQL：{0} 没有符合的记录！", sql);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
            throw new RuntimeException(e.getMessage());
        }
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
        ResultSetMetaData metaData = rs.getMetaData();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {// 遍历结果集
            String key = DataUtils.changeColumnToFieldName(metaData.getColumnLabel(i));
            Object value = rs.getObject(i);

            map.put(key, value);
        }

        return map;
    }

    /**
     * 查询单行记录(单个结果)，保存为 Map&lt;String, Object&gt; 结构。如果查询不到任何数据返回 null。
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return Map&lt;String, Object&gt; 结构的结果。如果查询不到任何数据返回 null。
     */
    public Map<String, Object> queryAsMap(String sql, Object... params) {
        return executeQuery(JdbcReader::getResultMap, sql, params);
    }

    /**
     * ResultSet 迭代器
     *
     * @param rs        结果集合
     * @param processor 单行处理器
     * @return 多行记录列表集合
     * @throws SQLException 异常
     */
    static <T> List<T> forEachRs(ResultSet rs, ResultSetProcessor<T> processor) throws SQLException {
        List<T> list = new ArrayList<>();

        do {
            T d = processor.process(rs);
            list.add(d);
        } while (rs.next());

//        return list.size() > 0 ? list : null; // 找不到记录返回 null，不返回空的 list
        return list; // 找不到记录返回 null，不返回空的 list
    }

    /**
     * 查询一组结果，保存为 List&lt;Map&lt;String, Object&gt;&gt; 结构。如果查询不到任何数据返回 null。
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return List&lt;Map&lt;String, Object&gt;&gt; 结构的结果。如果查询不到任何数据返回 null。
     */
    public List<Map<String, Object>> queryAsMapList(String sql, Object... params) {
        return executeQuery(rs -> forEachRs(rs, JdbcReader::getResultMap), sql, params);
    }

    /**
     * 有且只有一行记录，并只返回第一列的字段。可指定字段的数据类型
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param clz    期望的结果类型
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return 数据库里面的值作为 T 出现
     */
    @SuppressWarnings("unchecked")
    public <T> T queryOne(String sql, Class<T> clz, Object... params) {
        Map<String, Object> map = queryAsMap(sql, params);

        if (map == null)
            return null;

        for (String key : map.keySet()) {// 有且只有一个记录
            Object obj = map.get(key);

            if (obj == null)
                return null;
            else {
                return MyConvert.getConvertValue().cast(obj, clz);
//                if (obj instanceof Long && clz == int.class) {
//                    Object _int = ((Long) obj).intValue();
//                    return (T) _int;
//                }
//
//                if (obj instanceof Integer && (clz == long.class || clz == Long.class)) {
//                    Object _int = ((Integer) obj).longValue();
//                    return (T) _int;
//                }
//
//                return (T) obj;
            }
        }

        return null;
    }

    /**
     * 查询数组
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param clz    注意 Integer.class 不能用 int.class 代替
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return 数组结构的结果集合
     */
    @SuppressWarnings("unchecked")
    public <T> T[] queryArray(Class<T> clz, String sql, Object... params) {
        return executeQuery((ResultSet rs) -> {
            List<T> list = forEachRs(rs, _rs -> (T) rs.getObject(1));

            if (list == null)
                return null;

            Object array = Array.newInstance(clz, list.size());// List 转为数组 这里可否优化？

            for (int i = 0; i < list.size(); i++)
                Array.set(array, i, list.get(i));

            return (T[]) array;
        }, sql, params);
    }

    /**
     * 记录集合转换为 bean 的高阶函数
     *
     * @param beanClz 实体类
     * @return ResultSet 处理器，传入 ResultSet 类型对象返回 T 类型的 bean
     */
    @SuppressWarnings({"unchecked"})
    public static <T> ResultSetProcessor<T> getResultBean(Class<T> beanClz) {
        return rs -> {
            ResultSetMetaData metaData = rs.getMetaData();

            if (beanClz == Integer.class || beanClz == Long.class || beanClz == String.class || beanClz == Double.class || beanClz == Float.class || beanClz == BigDecimal.class) {
                Object v = rs.getObject(1);

                return (T) v;
//                for (int i = 1; i <= columnLength; i++) {// 遍历结果集
//                    Object v = rs.getObject(i);
//
//                    return (T) v;
//                }
            }

            T bean = NewInstance.newInstance(beanClz);

//            if (beanClz.toString().contains("xxx")) {
//                System.out.println();
//            }

            for (int i = 1; i <= metaData.getColumnCount(); i++) {// 遍历结果集
                String key = metaData.getColumnLabel(i);
                Object _value = rs.getObject(i); // Real value in DB

                if (key.contains("_")) // 将以下划线分隔的数据库字段转换为驼峰风格的字符串
                    key = DataUtils.changeColumnToFieldName(key);

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
                        value = MyConvert.getConvertValue().to(_value, propertyType);
                    } catch (NumberFormatException e) {
                        String input = (value == null ? " 空值 " : value.getClass().toString());
                        String expect = property.getPropertyType().toString();
//                        LOGGER.warning(e, "保存数据到 bean 的 {0} 字段时，转换失败，输入值：{1}，输入类型 ：{2}， 期待类型：{3}", key, value, input, expect);
                        continue; // 转换失败，继续下一个字段
                    }
//					}

                    Methods.executeMethod(bean, method, value);
                } catch (IntrospectionException | IllegalArgumentException e) {
//                    LOGGER.warning(e);

                    if (e instanceof IntrospectionException) {
                        // 数据库返回这个字段，但是 bean 没有对应的方法
//						LOGGER.info("数据库返回这个字段 {0}，但是 bean {1} 没有对应的方法", key, beanClz);
//						e.printStackTrace();

                        try {
                            if ((_value != null) && beanClz.getField("extractData") != null) {
                                assert bean != null;
                                Object obj = Methods.executeMethod(bean, "getExtractData");

//								LOGGER.info(":::::::::key::"+ key +":::v:::" + _value);
                                if (obj == null) {
                                    Map<String, Object> extractData = new HashMap<>();
                                    Methods.executeMethod(bean, "setExtractData", extractData);
                                    obj = Methods.executeMethod(bean, "getExtractData");
                                }

                                Map<String, Object> map = (Map<String, Object>) obj;
                                assert map != null;
                                map.put(key, _value);
                            }
                        } catch (NoSuchFieldException | SecurityException e1) {
//							 LOGGER.warning(e);
                        }
                    }
                }
            }

            return bean;
        };
    }

    /**
     * 查询单行记录(单个结果)，保存为 Bean。如果查询不到任何数据返回 null。
     *
     * @param <T>     实体类型
     * @param beanClz Bean 实体的类
     * @param sql     SQL 语句，可以带有 ? 的占位符
     * @param params  插入到 SQL 中的参数，可单个可多个可不填
     * @return 查询结果。如果查询不到任何数据返回 null。
     */
    public <T> T queryAsBean(Class<T> beanClz, String sql, Object... params) {
        return executeQuery(getResultBean(beanClz), sql, params);
    }

    /**
     * 查询一组结果，保存为 List&lt;Bean&gt; 结构。如果查询不到任何数据返回 null。
     *
     * @param beanClz Bean 实体的类
     * @param sql     SQL 语句，可以带有 ? 的占位符
     * @param params  插入到 SQL 中的参数，可单个可多个可不填
     * @return List&lt;Bean&gt; 结构的结果。如果查询不到任何数据返回 null。
     */
    public <T> List<T> queryAsBeanList(Class<T> beanClz, String sql, Object... params) {
        return executeQuery(rs -> forEachRs(rs, getResultBean(beanClz)), sql, params);
    }

}
