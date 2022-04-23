package com.ajaxjs.user.rbac;

/**
 * 用户角色权限系统的常量
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface RbacConstant {
	public static class RESOURCES_GROUP {
		public final static RESOURCES_GROUP COMMON = new RESOURCES_GROUP("基本业务");

		public final static RESOURCES_GROUP SYSTEM = new RESOURCES_GROUP("系统模块");

		public final static RESOURCES_GROUP USER = new RESOURCES_GROUP("用户系统");

		public final static RESOURCES_GROUP WEBSITE = new RESOURCES_GROUP("网站管理");

		/**
		 * 说明
		 */
		private String name;

		public RESOURCES_GROUP(String name) {
			this.setName(name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
