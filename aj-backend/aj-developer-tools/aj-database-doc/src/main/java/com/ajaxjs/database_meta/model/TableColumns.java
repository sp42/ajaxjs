package com.ajaxjs.database_meta.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 列详情
 *
 * @author Frank Cheung sp42@qq.com
 */
@Data
@Accessors(chain = true)
public class TableColumns {
    /**
     * 列名
     */
    private String field;

    /**
     * 数据类型
     */
    private String type;

    /**
     * 字符集排序规则
     */
    private String collation;

    /**
     * 是否允许为空
     */
    private String nu;

    /**
     * 键类型（主键、外键等）
     */
    private String key;

    /**
     * 默认值
     */
    private String defau;

    /**
     * 额外信息（自增、自动更新等）
     */
    private String extra;

    /**
     * 权限
     */
    private String privileges;

    /**
     * 注释
     */
    private String comment;
}
