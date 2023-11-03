package com.ajaxjs.base.business.model;

import java.util.Date;

import com.ajaxjs.data.jdbc_helper.common.IgnoreDB;
import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 操作日志
 */
@Data
public class ServiceLog implements IBaseModel {
    /**
     * 主键 id，自增
     */
    private Long id;

    /**
     * 简介、描述 collate utf8mb4_bin
     */
    private String name;

    /**
     * 操作者 id
     */
    private Long userid;

    /**
     * 租户 id。 0 = 不设租户
     */
    private Long tenantid;

    /**
     * 操作者 ip collate utf8mb4_bin
     */
    private String ip;

    /**
     * 相关执行的 sql collate utf8mb4_bin
     */
    private String sql;

    /**
     * 其他操作内容 collate utf8mb4_bin
     */
    private String content;

    /**
     * 操作异常，如果为空表示操作成功 collate utf8mb4_bin
     */
    private String error;

    /**
     * 是否操作成功
     */
    private Boolean done;

    /**
     * 创建时间
     */
    private Date createdate;

    public Boolean isDone() {
        done = error == null || "".equals(error);
        return done;
    }

    @IgnoreDB
    public Boolean getDone() {
        done = error == null || "".equals(error);
        return done;
    }

}