package com.ajaxjs.data_service.model;

import javax.sql.DataSource;

import com.ajaxjs.framework.BaseModel;

/**
 * 数据源模型
 *
 * @author Frank Cheung
 */
public class MyDataSource extends BaseModel {
	/**
	 * 数据源类型
	 */
//	private DB_Type type;
	private Integer type;

	/**
	 * 连接地址
	 */
	private String url;

	/**
	 * 应用 id
	 */
	private String appId;

	/**
	 * URL 目录
	 */
	private String urlDir;

	/**
	 * 登录用户
	 */
	private String username;

	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 是否连接验证成功
	 */
	private Boolean connect_ok;

	/**
	 * 是否跨库
	 */
	private Boolean isCrossDB;

	private DataSource instance;

	/**
	 * 项目名称
	 */
	private String projectName;

//	public DB_Type getType() {
//		return type;
//	}
//
//	public void setType(DB_Type type) {
//		this.type = type;
//	}

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

	public Boolean getConnect_ok() {
		return connect_ok;
	}

	public void setConnect_ok(Boolean connect_ok) {
		this.connect_ok = connect_ok;
	}

	public String getUrlDir() {
		return urlDir;
	}

	public void setUrlDir(String urlDir) {
		this.urlDir = urlDir;
	}

	public DataSource getInstance() {
		return instance;
	}

	public void setInstance(DataSource instance) {
		this.instance = instance;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getCrossDB() {
		return isCrossDB;
	}

	public void setCrossDB(Boolean crossDB) {
		isCrossDB = crossDB;
	}
}