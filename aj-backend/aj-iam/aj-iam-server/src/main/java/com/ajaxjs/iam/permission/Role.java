package com.ajaxjs.iam.permission;

import lombok.Data;

@Data
public class Role {
    /**
     * 主键 id，自增
     */
    private Integer id;

    /**
     * 父 id
     */
    private Integer parentId;

    /**
     * 租户 id
     */
    private Integer tenantId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 是否继承父级权限
     */
    private Boolean isInheritedParent;

    /**
     * 权限总值
     */
    private Long permissionValue;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 备注
     */
    private String content;

    /**
     * 数据字典：状态
     */
    private Integer stat;

    /**
     * 扩展 JSON 字段
     */
    private String extend;
}
