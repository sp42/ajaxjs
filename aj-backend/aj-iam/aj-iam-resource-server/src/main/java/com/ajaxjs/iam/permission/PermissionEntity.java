package com.ajaxjs.iam.permission;

import lombok.Data;

@Data
public class PermissionEntity {
    /**
     * CODE
     */
    private String name;

    /**
     * 权限的索引
     */
    private int index;

    /**
     * Data Object for permission
     */
    private Permission permission;

    public PermissionEntity(String name) {
        this.name = name;
    }

    /**
     * 一个用户有多个角色，来这里判断
     *
     * @param permissionValues 角色的权限值的集合
     * @return 是否有权限
     */
    public boolean check(long[] permissionValues) {
        for (long permissionValue : permissionValues)
            if (PermissionControl.check(permissionValue, index)) return true;

        return false;
    }

    /**
     * 检查权限值是否满足要求
     *
     * @param permissionValue 权限值
     * @return true表示满足要求，false表示不满足要求
     */
    public boolean check(long permissionValue) {
        return PermissionControl.check(permissionValue, index);
    }
}
