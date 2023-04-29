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
     *
     */
    private String comment;

    /**
     * 表 CREATE TABLE SQL
     */
    private String ddl;

    private List<Column> columns;
}
