package com.ajaxjs.framework.entity;

import com.ajaxjs.data.CRUD;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通用实体快速的 CRUD
 */
@Data
public class BaseEntity<T, K extends Serializable> {
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

    public List<T> list() {
        return list(null);
    }

    public List<T> list(String where) {
        String sql = "SELECT * FROM " + tableName + " WHERE 1=1 ";

        if (hasIsDeleted)
            sql += " AND " + delField + " = 0";

        if (where != null)
            sql += where;

        return CRUD.list(clz, sql);
    }
}
