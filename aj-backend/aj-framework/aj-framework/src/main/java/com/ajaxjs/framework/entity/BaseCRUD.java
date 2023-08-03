package com.ajaxjs.framework.entity;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.PageResult;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通用实体快速的 CRUD
 */
@Data
public class BaseCRUD<T, K extends Serializable> {
    /**
     * 主键名称
     */
    private String idField = "id";

    /**
     * 表名
     */
    private String tableName;

    /**
     * Bean 实体
     */
    private T entity;

    /**
     * 实体类引用
     */
    private Class<T> clz;

    /**
     * 是否有逻辑删除标记
     */
    private boolean hasIsDeleted;

    private String delField = "is_deleted";

    /**
     * 获取单笔记录
     */
    public T info(K id) {
        return CRUD.info(clz, "SELECT * FROM " + tableName + " WHERE " + idField + " = ?", id);
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
     * 获取列表
     *
     * @param where 查询条件
     * @return 列表
     */
    public List<T> list(String where) {
        String sql = getListSql(where);

        return CRUD.list(clz, sql);
    }

    private String getListSql(String where) {
        String sql = "SELECT * FROM " + tableName + " WHERE 1=1 ";

        if (hasIsDeleted)
            sql += " AND " + delField + " = 0";

        if (where != null)
            sql += where;

        return sql;
    }

    /**
     * 分页列表
     */
    public PageResult<T> page(String where) {
        String sql = getListSql(where);

        return CRUD.page(clz, sql, null);
    }

    public boolean delete(K id) {
        if (hasIsDeleted)
            CRUD.jdbcWriterFactory().write("UPDATE " + tableName + " SET " + delField + " = 1 WHERE " + idField + " = ?", id);
        else
            CRUD.jdbcWriterFactory().write("DELETE FROM " + tableName + " WHERE " + idField + " = ?", id);

        return true;
    }
}
