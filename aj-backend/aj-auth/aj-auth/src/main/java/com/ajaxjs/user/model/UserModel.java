package com.ajaxjs.user.model;

import java.util.Date;

import com.ajaxjs.framework.SimpleBaseModel;

public interface UserModel {
	/**
	 * 租户
	 */
	public static class Tenant extends SimpleBaseModel {
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
	public static class Org extends SimpleBaseModel {
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
	public static class User extends SimpleBaseModel {
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
