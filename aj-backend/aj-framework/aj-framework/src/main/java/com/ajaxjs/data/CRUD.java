package com.ajaxjs.data;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.data.jdbc_helper.JdbcReader;
import com.ajaxjs.data.jdbc_helper.JdbcWriter;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.entity.PageEnhancer;
import com.ajaxjs.framework.entity.SmallMyBatis;
import com.ajaxjs.framework.entity.TableName;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * CRUD
 */
public abstract class CRUD {
    private static final LogHelper LOGGER = LogHelper.getLog(CRUD.class);

    public static JdbcReader jdbcReaderFactory() {
        JdbcReader reader = new JdbcReader();
        reader.setConn(JdbcConn.getConnection());

        return reader;
    }

    public static JdbcWriter jdbcWriterFactory() {
        //  JdbcWriter 有较多前期的全局配置，故一般在注入阶段配置好
        JdbcWriter writer = DiContextUtil.getBean(JdbcWriter.class);
        assert writer != null;
        writer.setConn(JdbcConn.getConnection());

        return writer;
    }

    /**
     * 查询单笔记录，以 Map 格式返回
     *
     * @param sql    SQL 语句
     * @param params 参数列表，可以为 null 不传
     * @return 查询结果，如果为 null 表示没数据
     */
    public static Map<String, Object> info(String sql, Object... params) {
        return jdbcReaderFactory().queryAsMap(sql, params);
    }

    /**
     * 查询列表记录，以 List Map 格式返回
     *
     * @param sql    SQL 语句
     * @param params 参数列表，可以为 null 不传
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static List<Map<String, Object>> list(String sql, Object... params) {
        List<Map<String, Object>> mapList = jdbcReaderFactory().queryAsMapList(sql, params);

        return ListUtils.getList(mapList);
    }

    public static <T> PageResult<T> page(Class<T> beanClz, String sql, Map<String, Object> paramsMap) {
        sql = SmallMyBatis.handleSql(sql, paramsMap);

        return PageEnhancer.page(sql, beanClz);
    }

    public static <T> PageResult<T> page(String sqlId, Class<T> beanClz, Map<String, Object> paramsMap) {
        String sql = SmallMyBatis.handleSql(paramsMap, sqlId);

        return PageEnhancer.page(sql, beanClz);
    }

    public static <T> T info(String sqlId, Class<T> beanClz, Map<String, Object> paramsMap, Object... params) {
        String sql = SmallMyBatis.handleSql(paramsMap, sqlId);

        return jdbcReaderFactory().queryAsBean(beanClz, sql, params);
    }

    public static <T> T info(Class<T> beanClz, String sql, Object... params) {
        return jdbcReaderFactory().queryAsBean(beanClz, sql, params);
    }

    /**
     * 查询列表记录，以 List Map 格式返回
     *
     * @param beanClz 实体 Bean 类型
     * @param sql     SQL 语句
     * @param params  参数列表，可以为 null 不传
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static <T> List<T> list(Class<T> beanClz, String sql, Object... params) {
        List<T> list = jdbcReaderFactory().queryAsBeanList(beanClz, sql, params);

        return ListUtils.getList(list);
    }

    public static <T> List<T> list(String sqlId, Class<T> beanClz, Map<String, Object> paramsMap, Object... params) {
        String sql = SmallMyBatis.handleSql(paramsMap, sqlId);

        return list(beanClz, sql, params);
    }

    /**
     * 获取实体类上的表名（注解）
     *
     * @param entity 实体类
     * @return 表名
     */
    static String getTableName(Object entity) {
        TableName tableNameA = entity.getClass().getAnnotation(TableName.class);
        if (tableNameA == null)
            throw new RuntimeException("实体类未提供表名");

        return tableNameA.value();
    }

    public static Long create(String talebName, Object entity) {
        JdbcWriter jdbcWriter = jdbcWriterFactory();
        jdbcWriter.setTableName(talebName);

        return (Long) jdbcWriter.create(entity);
    }

    public static Long create(Object entity) {
        return create(getTableName(entity), entity);
    }

    public static boolean update(String talebName, Object entity, String idField) {
        JdbcWriter jdbcWriter = jdbcWriterFactory();
        jdbcWriter.setTableName(talebName);

        if (StringUtils.hasText(idField))
            jdbcWriter.setIdField(idField);

        return jdbcWriter.update(entity) > 0;
    }

    public static boolean update(String talebName, Object entity) {
        return update(talebName, entity, null);
    }

    public static boolean update(Object entity) {
        return update(getTableName(entity), entity);
    }

    public static boolean updateWithIdField(Object entity, String idField) {
        return update(getTableName(entity), entity, idField);
    }

    public static boolean delete(Object entity, Serializable id) {
        return delete(getTableName(entity), id);
    }

    public static boolean delete(String talebName, Serializable id) {
        JdbcWriter jdbcWriter = jdbcWriterFactory();
        jdbcWriter.setTableName(talebName);

        return jdbcWriter.delete(id);
    }

    public static boolean delete(Object entity) {
        Object id = ReflectUtil.executeMethod(entity, "getId");

        if (id != null)
            return delete(entity, (Serializable) id);
        else {
            LOGGER.warning("没有 getId()");
            return false;
        }
    }

}
