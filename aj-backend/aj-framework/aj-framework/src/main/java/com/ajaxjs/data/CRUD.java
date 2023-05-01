package com.ajaxjs.data;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.data.jdbc_helper.JdbcReader;
import com.ajaxjs.data.jdbc_helper.JdbcWriter;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.logger.LogHelper;

import java.util.List;
import java.util.Map;

/**
 * CRUD
 */
public abstract class CRUD {
    private static final LogHelper LOGGER = LogHelper.getLog(CRUD.class);

    private static JdbcReader jdbcReaderFactory() {
        JdbcReader reader = new JdbcReader();
        reader.setConn(JdbcConn.getConnection());

        return reader;
    }

    private static JdbcWriter jdbcWriterFactory() {
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

//    public static PageResult<Map<String, Object>> page(String sql, Object... params) {
//        return jdbcReaderFactory().queryAsMapList(sql, params);
//    }

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
}
