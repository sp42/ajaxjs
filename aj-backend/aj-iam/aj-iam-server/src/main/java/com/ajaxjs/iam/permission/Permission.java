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
     * 是否继承权限
     */
    public Boolean isInherit;
}
