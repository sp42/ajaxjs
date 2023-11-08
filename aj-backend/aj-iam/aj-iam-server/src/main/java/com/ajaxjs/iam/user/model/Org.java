package com.ajaxjs.iam.user.model;

import com.ajaxjs.framework.BaseModel;

/**
 * 组织
 */
public class Org extends BaseModel {
    /**
     * 部门 ID
     */
    public Long parentId;

    /**
     * 租户 id
     */
    public Long tenantId;
}