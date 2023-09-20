package com.ajaxjs.data;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.data.jdbc_helper.JdbcReader;
import com.ajaxjs.data.jdbc_helper.JdbcWriter;
import com.ajaxjs.data.jdbc_helper.common.IdField;
import com.ajaxjs.data.jdbc_helper.common.TableName;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.Methods;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * CRUD
 */
@Deprecated
public abstract class BaseCRUD {
    private static final LogHelper LOGGER = LogHelper.getLog(BaseCRUD.class);

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
     * @param params SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果为 null 表示没数据
     */
    @Deprecated
    public static Map<String, Object> info(String sql, Object... params) {
        return jdbcReaderFactory().queryAsMap(sql, params);
    }

    /**
     * 查询单笔记录，以 Map 格式返回
     *
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param params    SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果为 null 表示没数据
     */
    public static Map<String, Object> infoMap(String sqlId, Map<String, Object> paramsMap, Object... params) {
        String sql = SmallMyBatis.handleSql(paramsMap, sqlId);

        return info(sql, params);
    }

    /**
     * 查询单笔记录，以 Java Bean 格式返回
     *
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param beanClz   返回的 Bean 类型
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param params    SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @param <T>       返回的 Bean 类型
     * @return 查询单笔记录，以 Java Bea 格式返回
     */
    @Deprecated
    public static <T> T info(String sqlId, Class<T> beanClz, Map<String, Object> paramsMap, Object... params) {
        String sql = SmallMyBatis.handleSql(paramsMap, sqlId);

        return jdbcReaderFactory().queryAsBean(beanClz, sql, params);
    }

    /**
     * 查询单笔记录，以 Java Bean 格式返回
     *
     * @param beanClz 返回的 Bean 类型
     * @param sql     SQL 语句
     * @param params  SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @param <T>     返回的 Bean 类型
     * @return 查询单笔记录，以 Java Bea 格式返回
     */
    @Deprecated
    public static <T> T info(Class<T> beanClz, String sql, Object... params) {
        return jdbcReaderFactory().queryAsBean(beanClz, sql, params);
    }

    /**
     * 查询列表记录，以 List Map 格式返回
     *
     * @param sql    SQL 语句
     * @param params SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果没数据返回一个空 List
     */
    @Deprecated
    public static List<Map<String, Object>> list(String sql, Object... params) {
        List<Map<String, Object>> mapList = jdbcReaderFactory().queryAsMapList(sql, params);

        return ListUtils.getList(mapList);
    }

    /**
     * 查询列表记录，以 List Map 格式返回
     *
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param params    SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果没数据返回一个空 List
     */
    @Deprecated
    public static List<Map<String, Object>> listMap(String sqlId, Map<String, Object> paramsMap, Object... params) {
        String sql = SmallMyBatis.handleSql(paramsMap, sqlId);

        return list(sql, params);
    }

    /**
     * 查询列表记录，以 List Java Bean 格式返回
     *
     * @param beanClz 实体 Bean 类型
     * @param sql     SQL 语句
     * @param params  SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果没数据返回一个空 List
     */
    @Deprecated
    public static <T> List<T> list(Class<T> beanClz, String sql, Object... params) {
        List<T> list = jdbcReaderFactory().queryAsBeanList(beanClz, sql, params);

        return ListUtils.getList(list);
    }

    /**
     * 查询列表记录，以 List Java Bean 格式返回
     *
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param beanClz   实体 Bean 类型
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param params    SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @param <T>       实体 Bean 类型
     * @return 查询结果，如果没数据返回一个空 List
     */
    @Deprecated
    public static <T> List<T> list(String sqlId, Class<T> beanClz, Map<String, Object> paramsMap, Object... params) {
        return listBeanInXml(sqlId, beanClz, paramsMap, params);
    }

    /**
     * 查询列表记录，以 List Java Bean 格式返回
     *
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param beanClz   实体 Bean 类型
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param params    SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @param <T>       实体 Bean 类型
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static <T> List<T> listBeanInXml(String sqlId, Class<T> beanClz, Map<String, Object> paramsMap, Object... params) {
        String sql = SmallMyBatis.handleSql(paramsMap, sqlId);

        return list(beanClz, sql, params);
    }

    /**
     * 分页查询列表记录，以 List Java Bean 格式返回
     *
     * @param beanClz   实体 Bean 类型
     * @param sql       SQL 语句
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param <T>       实体 Bean 类型
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static <T> PageResult<T> page(Class<T> beanClz, String sql, Map<String, Object> paramsMap) {
        sql = SmallMyBatis.handleSql(sql, paramsMap);

        return PageEnhancer.page(sql, beanClz);
    }

    /**
     * 分页查询列表记录，以 List Java Bean 格式返回
     *
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param beanClz   实体 Bean 类型
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param <T>       实体 Bean 类型
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static <T> PageResult<T> page(String sqlId, Class<T> beanClz, Map<String, Object> paramsMap) {
        String sql = SmallMyBatis.handleSql(paramsMap, sqlId);

        return PageEnhancer.page(sql, beanClz);
    }
}
