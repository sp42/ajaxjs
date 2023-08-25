package com.ajaxjs.database_meta.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表索引信息
 */
@Data
@Accessors(chain = true)
public class TableIndex {
    /**
     * 表名
     */
    private String table;

    /**
     * 非唯一索引标志
     */
    private String nonUnique;

    /**
     * 索引名称
     */
    private String keyName;

    /**
     * 在索引中的位置
     */
    private String seqInIndex;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 字符集排序规则
     */
    private String collation;

    /**
     * 索引的基数
     */
    private String cardinality;

    /**
     * 子部分长度
     */
    private String subPart;

    /**
     * 压缩标志
     */
    private String packed;

    /**
     * 是否允许空值
     */
    private String nullValue;

    /**
     * 索引类型
     */
    private String indexType;

    /**
     * 注释
     */
    private String comment;

    /**
     * 索引注释
     */
    private String indexComment;

    /**
     * 是否可见
     */
    private String visible;

    /**
     * 表达式
     */
    private String expression;
}
