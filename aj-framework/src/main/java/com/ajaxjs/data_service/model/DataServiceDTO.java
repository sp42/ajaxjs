package com.ajaxjs.data_service.model;

import com.ajaxjs.framework.IBaseModel;

public interface DataServiceDTO {
	/**
	 * SQL 一张表的信息
	 */
	public static class TableInfo implements IBaseModel {
		/**
		 * 表名
		 */
		public String tableName;
		/**
		 * 表注释
		 */
		public String comment;
		/**
		 * 是否选中
		 */
		public boolean _checked;

	}
}
