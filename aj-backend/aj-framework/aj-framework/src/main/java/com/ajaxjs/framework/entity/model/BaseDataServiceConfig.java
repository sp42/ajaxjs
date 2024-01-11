package com.ajaxjs.framework.entity.model;

import lombok.Data;

/**
 * 数据服务的基础配置字段
 */
@Data
public class BaseDataServiceConfig {
    private Integer id;

    private Integer pid;

    /**
     * 说明
     */
    private String name;
    /**
     * 类型 SINGLE | CRUD
     */
    private String type;

    /**
     * 命名空间，标识
     */
    private String namespace;

    /**
     * 主键字段名称
     */
    private String idField = "id";

    /**
     * 表名
     */
    private String tableName;

    /**
     * 实体类引用名称
     */
    private String clzName;

    /**
     * 单条 SQL 命令
     */
    private String sql;

    /**
     * 查询详情的 SQL（可选的）
     */
    private String infoSql;

    /**
     * 查询列表的 SQL（可选的）
     */
    private String listSql;

    /**
     * 创建实体的 SQL（可选的）
     */
    private String createSql;

    /**
     * 修改实体的 SQL（可选的）
     */
    private String updateSql;

    /**
     * 删除实体的 SQL（可选的）
     */
    private String deleteSql;

    /**
     * 是否有逻辑删除标记
     */
    private boolean hasIsDeleted;

    /**
     * 是否加入租户数据隔离
     */
    private boolean isTenantIsolation;

    /**
     * 删除字段名称
     */
    private String delField = "is_deleted";

    /**
     * 1=自增；2=雪花；3=UUID
     */
    private Integer idType;
}
