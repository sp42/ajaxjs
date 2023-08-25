package com.ajaxjs.database_meta.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.util.List;

/**
 * 简单表信息
 *
 * @author Frank Cheung sp42@qq.com
 */
@Data
public class Table implements IBaseModel {
    private String uuid;

    /**
     * 表名
     */
    private String name;

    /**
     * 注释
     */
    private String comment;

    /**
     * 表 CREATE TABLE SQL
     */
    private String ddl;

    /**
     * 所有列的定义
     */
    private List<Column> columns;
}
