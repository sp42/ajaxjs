package com.ajaxjs.data;

import com.ajaxjs.data.jdbc_helper.JdbcReader;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.ListUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class CRUD2<T> {
    private JdbcReader reader;

    /**
     * SQL XML 里的 id
     */
    private String sqlId;

    /**
     * SQL 语句
     */
    private String sql;

    private Map<String, Object> mapParams;

    private Class<T> beanClz;

    private Object[] orderedParams;

    /**
     * 获取 SQL 语句，如果是 SQL Id 则获取并解析之
     *
     * @return 最终 SQL 语句
     */
    private String getRealSql() {
        if (StringUtils.hasText(sql)) return sql;
        else if (StringUtils.hasText(sqlId)) return SmallMyBatis.handleSql(mapParams, sqlId);
        else throw new IllegalArgumentException("没输入的 SQL 参数");
    }

    /**
     * 获取单笔详情
     *
     * @return 可能是 Map 或者 Java Bean，根据 beanClz 是否有值来决定
     */
    public Object info() {
        String sql = getRealSql();

        return beanClz == null ? reader.queryAsMap(sql, orderedParams) : reader.queryAsBean(beanClz, sql, orderedParams);
    }

    @SuppressWarnings("unchecked")
    public T infoBean() {
        Object info = info();
        if (info == null) return null;

        return (T) info;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> infoMap() {
        Object info = info();
        if (info == null) return null;

        return (Map<String, Object>) info;
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
    public static <T> T info(Class<T> beanClz, String sql, Object... params) {
        return new CRUD2<T>().setBeanClz(beanClz).setSql(sql).setOrderedParams(params).infoBean();
    }

    /**
     * 查询单笔记录，以 Java Bean 格式返回
     *
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param beanClz   返回的 Bean 类型
     * @param mapParams Map 格式的参数（若没有可传 null）
     * @param params    SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @param <T>       返回的 Bean 类型
     * @return 查询单笔记录，以 Java Bea 格式返回
     */
    public static <T> T infoBySqlId(Class<T> beanClz, String sqlId, Map<String, Object> mapParams, Object... params) {
        return new CRUD2<T>().setBeanClz(beanClz).setSqlId(sqlId).setMapParams(mapParams).setOrderedParams(params).infoBean();
    }

    /**
     * 查询单笔记录，以 Map 格式返回
     *
     * @param sql    SQL 语句
     * @param params SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果为 null 表示没数据
     */
    public static Map<String, Object> infoMap(String sql, Object... params) {
        return new CRUD2<>().setSql(sql).setOrderedParams(params).infoMap();
    }

    /**
     * 查询单笔记录，以 Map 格式返回
     *
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param mapParams Map 格式的参数（若没有可传 null）
     * @param params    SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果为 null 表示没数据
     */
    public static Map<String, Object> infoMapBySqlId(String sqlId, Map<String, Object> mapParams, Object... params) {
        return new CRUD2<>().setSqlId(sqlId).setMapParams(mapParams).setOrderedParams(params).infoMap();
    }

    public List<?> list() {
        String sql = getRealSql();

        return beanClz == null ? reader.queryAsMapList(sql, orderedParams) : reader.queryAsBeanList(beanClz, sql, orderedParams);
    }

    @SuppressWarnings("unchecked")
    public List<T> listBean() {
        List<T> list = (List<T>) list();

        return ListUtils.getList(list);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> listMap() {
        List<Map<String, Object>> list = (List<Map<String, Object>>) list();

        return ListUtils.getList(list);
    }

    /**
     * 查询列表记录，以 List Map 格式返回
     *
     * @param sql    SQL 语句
     * @param params SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static List<Map<String, Object>> listMap(String sql, Object... params) {
        return new CRUD2<>().setSql(sql).setOrderedParams(params).listMap();
    }

    /**
     * 查询列表记录，以 List Map 格式返回
     *
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param params    SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static List<Map<String, Object>> listMapBySqlId(String sqlId, Map<String, Object> paramsMap, Object... params) {
        return new CRUD2<>().setSqlId(sqlId).setMapParams(paramsMap).setOrderedParams(params).listMap();
    }

    /**
     * 查询列表记录，以 List Java Bean 格式返回
     *
     * @param beanClz 实体 Bean 类型
     * @param sql     SQL 语句
     * @param params  SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static <T> List<T> list(Class<T> beanClz, String sql, Object... params) {
        return new CRUD2<T>().setBeanClz(beanClz).setSql(sql).setOrderedParams(params).listBean();
    }

    /**
     * 查询列表记录，以 List Java Bean 格式返回
     *
     * @param beanClz   实体 Bean 类型
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param params    SQL 参数列表（选填项，能对应 SQL 里面的`?`的插值符）
     * @param <T>       实体 Bean 类型
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static <T> List<T> list(Class<T> beanClz, String sqlId, Map<String, Object> paramsMap, Object... params) {
        return new CRUD2<T>().setBeanClz(beanClz).setSqlId(sqlId).setMapParams(paramsMap).setOrderedParams(params).listBean();
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
     * @param beanClz   实体 Bean 类型
     * @param sqlId     SQL Id，于 XML 里的索引
     * @param paramsMap Map 格式的参数（若没有可传 null）
     * @param <T>       实体 Bean 类型
     * @return 查询结果，如果没数据返回一个空 List
     */
    public static <T> PageResult<T> pageBySqlId(Class<T> beanClz, String sqlId, Map<String, Object> paramsMap) {
        String sql = SmallMyBatis.handleSql(paramsMap, sqlId);

        return PageEnhancer.page(sql, beanClz);
    }
}
