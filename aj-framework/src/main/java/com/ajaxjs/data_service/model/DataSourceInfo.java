package com.ajaxjs.data_service.model;

import com.ajaxjs.data_service.DataServiceConstant.DatabaseType;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;

/**
 * 数据库的数据源
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class DataSourceInfo extends BaseModel implements IBaseModel {
	/**
	 * 数据库厂商：my_sql， sql_server， oracle， postgre_sql， db2， sqlite， spark
	 */
	private DatabaseType type;

	/**
	 * 连接地址
	 */
	private String url;

	/**
	 * 数据源编码，唯一
	 */
	private String urlDir;

	/**
	 * 数据库用户账号
	 */
	private String username;

	/**
	 * 数据库账号密码
	 */
	private String password;

	/**
	 * 是否跨库
	 */
	private Boolean crossDb;

	public DatabaseType getType() {
		return type;
	}

	public void setType(DatabaseType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getCrossDb() {
		return crossDb;
	}

	public void setCrossDb(Boolean crossDb) {
		this.crossDb = crossDb;
	}

	public String getUrlDir() {
		return urlDir;
	}

	public void setUrlDir(String urlDir) {
		this.urlDir = urlDir;
	}

}
