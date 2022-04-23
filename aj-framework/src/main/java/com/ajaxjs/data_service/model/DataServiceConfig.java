package com.ajaxjs.data_service.model;

import javax.sql.DataSource;

/**
 * 数据服务的配置
 */
public class DataServiceConfig {
	/**
	 * 是否嵌入模式（是否单数据源）
	 */
	private boolean embed;

	/**
	 * 数据源，配置表所在的数据源。当 isEmbed = true 时也是业务数据的数据源
	 */
	private DataSource dataSource;

	public boolean isEmbed() {
		return embed;
	}

	public void setEmbed(boolean embed) {
		this.embed = embed;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
