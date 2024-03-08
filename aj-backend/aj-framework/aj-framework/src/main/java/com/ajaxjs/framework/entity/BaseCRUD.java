package com.ajaxjs.framework.entity;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.data.SmallMyBatis;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.data.util.SnowflakeId;
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

    public static long getCurrentUserId() {
        Object simpleUser = Objects.requireNonNull(DiContextUtil.getRequest()).getAttribute("USER_KEY_IN_REQUEST");
        if (simpleUser == null) throw new NullPointerException("用户不存在");

        return executeMethod(simpleUser, "getId", long.class);
    }

    // user_id locks
    private String limitToCurrentUser(String sql) {
        if (isCurrentUserOnly()) {
            String add = " AND user_id = " + getCurrentUserId();

            if (sql.contains(DUMMY_STR))
                sql = sql.replace(DUMMY_STR, DUMMY_STR + add);
            else
                sql += add;
        }

        return sql;
    }

    public static <T> T executeMethod(Object obj, String methodName, Class<T> clz) {
        try {
            Method method = obj.getClass().getMethod(methodName);
            Object result = method.invoke(obj);

            return (T) result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
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
