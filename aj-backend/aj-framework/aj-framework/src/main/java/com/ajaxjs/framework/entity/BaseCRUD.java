package com.ajaxjs.framework.entity;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.data.SmallMyBatis;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.data.util.SnowflakeId;
import com.ajaxjs.framework.entity.model.BaseDataServiceConfig;
import com.ajaxjs.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
        } else
            sql = String.format(SELECT_SQL, getTableName()).replace(DUMMY_STR, getIdField() + " = ?");

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
        } else
            sql = String.format(SELECT_SQL, getTableName());

        if (isHasIsDeleted())
            sql = sql.replace(DUMMY_STR, DUMMY_STR + " AND " + getDelField() + " = 0");

        if (isTenantIsolation())
            sql = TenantService.addTenantIdQuery(sql);

        if (where != null)
            sql = sql.replace(DUMMY_STR, DUMMY_STR + where);

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
        if (isHasIsDeleted())
            CRUD.jdbcWriterFactory().write("UPDATE " + getTableName() + " SET " + getDelField() + " = 1 WHERE " + getIdField() + " = ?", id);
        else
            CRUD.jdbcWriterFactory().write("DELETE FROM " + getTableName() + " WHERE " + getIdField() + " = ?", id);

        return true;
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

        if (beforeCreate != null)
            beforeCreate.accept(params);

        Integer tenantId = TenantService.getTenantId();

        if (tenantId != null)
            params.put("tenant_id", tenantId);

        return (K) CRUD.create(getTableName(), params, getIdField());
    }

    public Boolean update(Map<String, Object> params) {
        String tableName = getTableName();
        String idField = getIdField();

        return CRUD.update(tableName, params, idField);
    }
}
