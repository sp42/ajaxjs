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
    private String field;
    private String type;
    private String collation;
    private String nu;
    private String key;
    private String defau;
    private String extra;
    private String privileges;
    private String comment;
}
