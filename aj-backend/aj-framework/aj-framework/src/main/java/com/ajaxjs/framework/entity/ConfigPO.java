package com.ajaxjs.framework.entity;

import lombok.Data;

import java.util.Date;

/**
 * 通用接口的数据库配置 PO
 */
@Data
public class ConfigPO {
    private Integer id;

    /**
     * 说明
     */
    private String name;

    /**
     * 命名空间，标识
     */
    private String namespace;

    /**
     * 主键字段名称
     */
    private String idField;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 实体类引用名称
     */
    private String clzName;

    /**
     * 查询详情的 SQL（可选的）
     */
    private String infoSql;

    /**
     * 查询列表的 SQL（可选的）
     */
    private String listSql;

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
    private String delField;

    /**
     * 1=自增；2=雪花；3=UUID
     */
    private Integer idType;

    private Date createDate;
}
