package com.ajaxjs.iam.permission;

import lombok.Data;

/**
 * 权限
 */
@Data
public class Permission {
    public Integer id;

    public String name;

    public String content;

    /**
     * 角色名称，在显示父级权限时候，有需要显示一下
     */
    public String roleName;

    /**
     * 是否继承权限
     */
    public Boolean isInherit;
}
