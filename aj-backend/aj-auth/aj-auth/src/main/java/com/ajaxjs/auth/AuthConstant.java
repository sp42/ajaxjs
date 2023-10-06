package com.ajaxjs.auth;

/**
 * 系统常量
 */
public interface AuthConstant {
	/**
	 * 应用类型 1.html web应用；2.API service应用；3.Web APi混合应用；4.原生应用；5.其他应用
	 */
	public static enum SystemAppType {
		HTML, API_SERVICE, RPC_SERVICE, DESKTOP, NATIVE, MISC;
	}

	/**
	 * 功能类型
	 */
	public static enum FunctionType {
		/**
		 * 页面地址或路由地址
		 */
		URL,

		/**
		 * 页面元素，如按钮
		 */
		ELEMENT;
	}

	/**
	 * 权限类型
	 */
	public static enum PermissionType {
		FUNCTION, API;
	}
}
