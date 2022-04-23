package com.ajaxjs.data_service.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量
 *
 * @author Frank Cheung
 */
public interface DataServiceConstant {
	/**
	 * 数据库类型
	 *
	 * @author Frank Cheung
	 */
	public static interface DBType {
		public static final int MYSQL = 1;
		public static final int ORACLE = 2;
		public static final int SQLSERVER = 3;
		public static final int SPARK = 4;
		public static final int SQLITE = 5;

		public static final Map<Integer, String> MAP = new HashMap<Integer, String>() {
			private static final long serialVersionUID = -1L;

			{
				put(MYSQL, "MySql");
				put(ORACLE, "Oracle");
				put(SQLSERVER, "SqlServer");
				put(SPARK, "Spark");
				put(SQLITE, "SQLite");
			}
		};
	}

	/**
	 * 分页模式
	 */
	public enum GET_LIST {
		/**
		 * Page:No/Size 分页方式
		 */
		PAGE_NO(2, "Page:No/Size"),

		/**
		 * start/limit 分页方式
		 */
		START_LIMIT(1, "start/limit "),

		/**
		 * 不分页
		 */
		NO_PAGE(0, "不分页");

		/**
		 * 常量
		 */
		public int value;

		/**
		 * 描述
		 */
		public String desc;

		/**
		 * 分页模式
		 *
		 * @param value 常量
		 * @param desc  描述
		 */
		GET_LIST(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 表示增删改查的几个状态
	 *
	 * @author Frank Cheung
	 */
	public static enum CRUD {
		/**
		 * 读取单笔记录
		 */
		INFO,

		/**
		 * 读取列表记录
		 */
		LIST,

		/**
		 * 分页读取列表记录
		 */
		PAGE_LIST,

		/**
		 * 创建记录
		 */
		CREATE,

		/**
		 * 更新
		 */
		UPDATE,

		/**
		 * 删除
		 */
		DELETE;

		/**
		 * 是否可读（创建、更新、删除）
		 *
		 * @return true 表示可读
		 */
		public boolean isWrite() {
			return this == CREATE || this == UPDATE || this == DELETE;
		}
	}

	/**
	 * 主键生成策略
	 */
	public static interface KeyGen {
		public final static int AUTO_INCRE = 1;
		public final static int SNOWFLAKE = 2;
		public final static int UUID = 3;
	}
}
