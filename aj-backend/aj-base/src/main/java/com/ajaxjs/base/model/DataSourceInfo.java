package com.ajaxjs.base.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.sql.DataSource;

/**
 * 数据库的数据源
 *
 * @author Frank Cheung sp42@qq.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DataSourceInfo extends BaseModel implements IBaseModel {
    /**
     * 数据库厂商：my_sql， sql_server， oracle， postgres_sql， db2， sqlite， spark
     */
    private DataServiceConstant.DatabaseType type;

    /**
     * 连接地址
     */
    private String url;

    /**
     * 数据源编码，唯一
     */
    private String urlDir;

    /**
     * 数据库用户账号
     */
    private String username;

    /**
     * 数据库账号密码
     */
    private String password;

    /**
     * 是否跨库
     */
    private Boolean crossDb;

    /**
     * 数据源实例
     */
    private DataSource instance;
}
