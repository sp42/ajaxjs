package com.ajaxjs.auth.model;

import java.util.Date;

import com.ajaxjs.auth.common.AuthConstant;
import com.ajaxjs.framework.SimpleBaseModel;

public interface SystemModel extends AuthConstant {
	/**
	 * 系统
	 */
	public static class Systematic extends SimpleBaseModel {
		/**
		 * 开发厂商
		 */
		public String devFirm;

		/**
		 * 联系人及联系方式
		 */
		public String contact;

		/**
		 * 图标
		 */
		public String icon;
	}

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
