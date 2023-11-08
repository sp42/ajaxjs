package com.ajaxjs.iam.user.model;

import java.util.Date;

public interface UserModel {
    /**
     * 租户
     */
    class Tenant  {
        /**
         * 租户编码
         */
        public String code;

        /**
         * 1、账号密码 2、二维码 3、手机验证码 4、微信授权 5、企业微信授权，一个租户类型可以选择多个登录方式，采用逗号隔开的方式
         */
        public String loginModes;
    }

    /**
     * 组织
     */
    class Org {
        /**
         * 部门 ID
         */
        public Long parentId;

        /**
         * 租户 id
         */
        public Long tenantId;
    }

    /**
     * 用户
     */
    class User {
        /**
         * 部门 ID
         */
        public Long orgId;

        /**
         * 租户 id
         */
        public Long tenantId;

        /**
         * 性别
         */
        public Date gender;

        /**
         * 出生日期
         */
        public Date birthday;

        /**
         * 头像
         */
        public String avatar;
    }
}
