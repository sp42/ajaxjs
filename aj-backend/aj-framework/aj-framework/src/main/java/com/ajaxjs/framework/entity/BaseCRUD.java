package com.ajaxjs.framework.entity;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.data.SmallMyBatis;
import com.ajaxjs.data.util.SnowflakeId;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.entity.model.BaseDataServiceConfig;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 通用实体快速的 CRUD
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseCRUD<T, K extends Serializable> extends BaseDataServiceConfig {
    /**
     * Bean 实体
     */
    private T entity;

    /**
     * 实体类引用
     */
    private Class<T> clz;

    /**
     * 子配置
     */
    private Map<String, BaseCRUD<?, Long>> children;

    /**
     * 1=自增；2=雪花；3=UUID
     */
    private Integer idType = BaseEntityConstants.IdType.AUTO_INC;

    private final static String DUMMY_STR = "1=1";

    private final static String SELECT_SQL = "SELECT * FROM %s WHERE 1=1 ORDER BY create_date DESC"; // 日期暂时写死

    private String getManagedInfoSql() {
        String sql = getInfoSql();

        if (StringUtils.hasText(sql)) {
            Map<String, Object> queryStringParams = DataServiceUtils.getQueryStringParams();
            sql = SmallMyBatis.handleSql(sql, queryStringParams);
        } else sql = String.format(SELECT_SQL, getTableName()).replace(DUMMY_STR, DUMMY_STR + " AND " + getIdField() + " = ?");

        sql = limitToCurrentUser(sql);

        return isTenantIsolation() ? TenantService.addTenantIdQuery(sql) : sql;
    }

    /**
     * 获取单笔记录
     */
    public T info(K id) {
        return CRUD.info(clz, getManagedInfoSql(), id);
    }

    /**
     * 获取单笔记录
     */
    public Map<String, Object> infoMap(K id) {
        return CRUD.infoMap(getManagedInfoSql(), id);
    }

    /**
     * 获取列表
     *
     * @return 列表
     */
    public List<T> list() {
        return list(null);
    }

    /**
     * 获取列表（Map 格式）
     *
     * @return 列表（Map 格式）
     */
    public List<Map<String, Object>> listMap() {
        String sql = getListSql(null);

        return CRUD.listMap(sql);
    }

    private String getListSql(String where) {
        String sql = getListSql();

        if (StringUtils.hasText(sql)) {
            sql = SmallMyBatis.handleSql(sql, DataServiceUtils.getQueryStringParams());
        } else sql = String.format(SELECT_SQL, getTableName());

        if (isHasIsDeleted()) sql = sql.replace(DUMMY_STR, DUMMY_STR + " AND " + getDelField() + " != 1");

        sql = limitToCurrentUser(sql);

        if (isTenantIsolation()) sql = TenantService.addTenantIdQuery(sql);

        if (where != null) sql = sql.replace(DUMMY_STR, DUMMY_STR + where);

        return sql;
    }

    /**
     * 获取列表
     *
     * @param where 查询条件
     * @return 列表
     */
    public List<T> list(String where) {
        String sql = getListSql(where);

        return CRUD.list(clz, sql);
    }

    /**
     * 分页列表
     */
    public PageResult<T> page(String where) {
        String sql = getListSql(where);

        return CRUD.page(clz, sql, null);
    }

    public boolean delete(K id) {
        String sql;

        if (isHasIsDeleted()) sql = "UPDATE " + getTableName() + " SET " + getDelField() + " = 1 WHERE " + getIdField() + " = ?";
        else sql = "DELETE FROM " + getTableName() + " WHERE " + getIdField() + " = ?";

        sql = limitToCurrentUser(sql);
        CRUD.jdbcWriterFactory().write(sql, id);

        return true;
    }

    /**
     * 获取上下文当前用户
     * IAM 的 SimpleUser 这里不通用于是用反射获取（一个方案是用 map，但麻烦）
     * 该方法会从请求中获取名为"USER_KEY_IN_REQUEST"的属性，该属性预期为一个简单的用户对象。
     * 如果该属性不存在会抛出 NullPointerException。
     *
     * @return 上下文当前用户
     * @throws NullPointerException 如果请求中不存在名为"USER_KEY_IN_REQUEST"的属性，表示用户不存在。
     */
    public static Object getCurrentUser() {
        // 从请求中获取名为"USER_KEY_IN_REQUEST"的属性，确保该属性不为空
        Object simpleUser = Objects.requireNonNull(DiContextUtil.getRequest()).getAttribute("USER_KEY_IN_REQUEST");
        if (simpleUser == null) throw new NullPointerException("上下文的用户不存在"); // 如果用户对象为空，则抛出异常

        return simpleUser;
    }

    /**
     * 获取当前用户的 ID。
     *
     * @return 当前用户的 ID，类型为 long。
     */
    public static long getCurrentUserId() {
        return executeMethod(getCurrentUser(), "getId", long.class);// 调用用户对象的 getId 方法，返回用户的 ID
    }

    /**
     * 获取当前用户的租户 ID。
     *
     * @return 当前用户的租户 ID，类型为 long。
     */
    public static Integer getCurrentUserTenantId() {
        return executeMethod(getCurrentUser(), "getTenantId", Integer.class);// 调用用户对象的 getId 方法，返回用户的 ID
    }

    /**
     * 对给定的 SQL 查询语句进行限制，确保只查询当前用户的数据。
     * 如果当前配置为只查询当前用户的数据，将在 SQL 语句中添加条件“user_id = 当前用户 ID”。
     * 如果提供的 SQL 语句中已包含特定的占位符（DUMMY_STR），则会将条件追加到该占位符之后，
     * 否则，将条件直接追加到 SQL 语句末尾。
     *
     * @param sql 初始的 SQL 查询语句。
     * @return 经过限制条件添加后的 SQL 查询语句。
     */
    private String limitToCurrentUser(String sql) {
        if (isCurrentUserOnly()) { // 检查是否配置为只查询当前用户的数据
            String add = " AND user_id = " + getCurrentUserId(); // 构造添加的查询条件

            if (sql.contains(DUMMY_STR)) // 检查SQL语句中是否已包含占位符
                sql = sql.replace(DUMMY_STR, DUMMY_STR + add); // 将条件插入到占位符之后
            else
                sql += add; // 直接将条件追加到SQL语句末尾
        }

        return sql; // 返回修改后的SQL语句
    }

    /**
     * 执行对象上的指定方法，并返回方法的执行结果。
     *
     * @param obj        要执行方法的对象实例。
     * @param methodName 要执行的方法的名称。
     * @param clz        期望的返回类型。
     * @return 方法执行的结果，其类型为参数 clz 指定的类型。
     * @throws RuntimeException 如果无法找到方法、访问方法失败或方法调用抛出异常，则抛出此运行时异常。
     */
    @SuppressWarnings("unchecked")
    public static <T> T executeMethod(Object obj, String methodName, Class<T> clz) {
        try {
            Method method = obj.getClass().getMethod(methodName);// 获取对象的类，然后通过方法名查找并返回方法对象。
            Object result = method.invoke(obj); // 调用方法并获取结果

            return (T) result;// 将结果强制转换为期望的类型并返回
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e); // 如果在执行过程中遇到异常，则将其封装并抛出为运行时异常
        }
    }

    /**
     * 创建之前的执行的回调函数，可以设置 createDate, createBy 等字段
     */
    private Consumer<Map<String, Object>> beforeCreate;

    /**
     * 创建实体
     *
     * @param params 实体
     * @return NewlyId
     */
    @SuppressWarnings("unchecked")
    public K create(Map<String, Object> params) {
        if (idType != null) { // auto increment by default
            if (idType == 2) {
                params.put(getIdField(), SnowflakeId.get());
            }

            if (idType == 3) {
                params.put(getIdField(), StrUtil.uuid());
            }
        }

        if (beforeCreate != null) beforeCreate.accept(params);

        Integer tenantId = TenantService.getTenantId();

        if (tenantId != null) params.put("tenant_id", tenantId);

        if (isCurrentUserOnly())
            params.put("user_id", getCurrentUserId());

        return (K) CRUD.create(getTableName(), params, getIdField());
    }

    public Boolean update(Map<String, Object> params) {
        String tableName = getTableName();
        String idField = getIdField();

        if (isCurrentUserOnly()) {
            Object id = params.get(idField);
            T info = info((K) id);

            if (info == null)
                throw new SecurityException("不能修改实体，id：" + id);
        }

        return CRUD.update(tableName, params, idField);
    }
}
