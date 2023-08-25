package com.ajaxjs.database_meta.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.util.List;

/**
 * 库，包含
 *
 * @author Frank Cheung sp42@qq.com
 */
@Data
public class Database implements IBaseModel {
    private String uuid;

    /**
     * 库名
     */
    private String name;

    /**
     * 库里面所有的表名
     */
    private List<String> tables;

    /**
     * 所有表的信息
     */
    private List<Table> tableInfo;
}
