package com.ajaxjs.iam.user.model;

import com.ajaxjs.iam.server.common.IamConstants;

import java.util.Date;

public interface PermissionModel {
    /**
     * 角色
     */
    class Role {
        /**
         * 应用 id
         */
        public Long appId;

        /**
         * 租户 id
         */
        public Long tenantId;

        /**
         * 角色编码
         */
        public String code;

        /**
         * 角色类型
         */
//		public RoleType type;
    }

    /**
     * 功能
     */
    class Function {
        /**
         * 父级 id
         */
        public Long parentId;

        /**
         * 路径
         */
        public String path;

        /**
         * 应用 id
         */
        public Date appId;

        /**
         * 角色名称
         */
        public String name;

        /**
         * 功能编码
         */
        public String code;

        /**
         * 页面地址/路由地址
         */
        public String url;

        /**
         * 权限类别【URL=页面地址或路由地址/ELEMENT= 页面元素，如按钮】
         */
//		public FunctionType type;

        /**
         * 排序号
         */
        public Integer seq;
    }

    /**
     * API
     */
    class Api {
        /**
         * 應用 id
         */
        public Long appId;

        /**
         * API 请求相对地址(ANT 风格路径表达式)
         */
        public String url;
    }

    /**
     * 角色权限
     */
    class RolePermission {
        /**
         * 應用 id
         */
        public Long appId;

        /**
         * 角色ID
         */
        public Long roleId;

        /**
         * 权限ID
         */
        public Long permissionId;

        /**
         * 权限类型【FUNCTION/API】
         */
        public IamConstants.PermissionType type;

        /**
         * 1=叶子权限，非父级权限 0/null=父级权限
         */
        public Boolean isLeaf;
    }

    /**
     * 角色权限
     */
    class UserRole {
        /**
         * 用户 id
         */
        public Long userId;

        /**
         * 角色ID
         */
        public Long roleId;
    }

}
