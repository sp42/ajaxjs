package com.ajaxjs.database_meta.model;

import lombok.Data;

/**
 * 列信息
 *
 * @author frank
 */
@Data
public class Column {
    /**
     * 类型
     */
    private String name;

    /**
     * 数据长度
     */
    private Integer length;

    /**
     * 类型
     */
    private String type;

    /**
     * 空值
     */
    private Boolean isRequired;

    /**
     * 注释
     */
    private String comment;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否主键
     */
    private Boolean isKey;

}
