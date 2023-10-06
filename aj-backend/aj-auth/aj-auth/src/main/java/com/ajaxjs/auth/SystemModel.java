package com.ajaxjs.auth;

import java.util.Date;

import com.ajaxjs.framework.SimpleBaseModel;

public interface SystemModel extends AuthConstant {
	/**
	 * 应用/客户端
	 */
	public class SystemApp extends SimpleBaseModel {
		/**
		 * 系统 id
		 */
		public Date sysId;

		/**
		 * 名称
		 */
		public String name;

		/**
		 * 简介
		 */
		public String content;

		/**
		 * 客户端 id
		 */
		public String clientId;

		/**
		 * 客户端秘钥
		 */
		public String clientSecret;

		/**
		 * 应用类型 1.html web应用；2.API service应用；3.Web APi混合应用；4.原生应用；5.其他应用
		 */
		public SystemAppType type;
	}
}
