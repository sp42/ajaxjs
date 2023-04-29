package com.ajaxjs.mysql.model;

import java.util.Map;

/**
 * 数据库详情
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class DataBaseDetail {
	/**
	 * 版本
	 */
	private String version;

	/**
	 * MYSQL 安装目录
	 */
	private String mySqlHome;

	private String basedir;

	/**
	 * MySQL 数据存放的位置
	 */
	private String datadir;

	/**
	 * 数据库 大小简介
	 */
	private Map<String, String> dbSize;

	/**
	 * 数据库编码信息
	 */
	private Map<String, String> charMap;

	/**
	 * 错误日志
	 */
	private Map<String, String> logError;

	/**
	 * 二进制日志
	 */
	private Map<String, String> logBin;

	/**
	 * 通用日志
	 */
	private Map<String, String> generalLog;

	/**
	 * 慢查询日志
	 */
	private Map<String, String> slowQueryLog;

	/**
	 * 最大连接数
	 */
	private Map<String, String> maxConnecttion;

	/**
	 * 线程数
	 */
	private Map<String, String> threads;

	/**
	 * table lock
	 */
	private Map<String, String> tableLock;

	private Map<String, String> variables;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMySqlHome() {
		return mySqlHome;
	}

	public void setMySqlHome(String mySqlHome) {
		this.mySqlHome = mySqlHome;
	}

	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	public String getDatadir() {
		return datadir;
	}

	public void setDatadir(String datadir) {
		this.datadir = datadir;
	}

	public Map<String, String> getDbSize() {
		return dbSize;
	}

	public void setDbSize(Map<String, String> dbSize) {
		this.dbSize = dbSize;
	}

	public Map<String, String> getCharMap() {
		return charMap;
	}

	public void setCharMap(Map<String, String> charMap) {
		this.charMap = charMap;
	}

	public Map<String, String> getLogError() {
		return logError;
	}

	public void setLogError(Map<String, String> logError) {
		this.logError = logError;
	}

	public Map<String, String> getLogBin() {
		return logBin;
	}

	public void setLogBin(Map<String, String> logBin) {
		this.logBin = logBin;
	}

	public Map<String, String> getGeneralLog() {
		return generalLog;
	}

	public void setGeneralLog(Map<String, String> generalLog) {
		this.generalLog = generalLog;
	}

	public Map<String, String> getSlowQueryLog() {
		return slowQueryLog;
	}

	public void setSlowQueryLog(Map<String, String> slowQueryLog) {
		this.slowQueryLog = slowQueryLog;
	}

	public Map<String, String> getMaxConnecttion() {
		return maxConnecttion;
	}

	public void setMaxConnecttion(Map<String, String> maxConnecttion) {
		this.maxConnecttion = maxConnecttion;
	}

	public Map<String, String> getThreads() {
		return threads;
	}

	public void setThreads(Map<String, String> threads) {
		this.threads = threads;
	}

	public Map<String, String> getTableLock() {
		return tableLock;
	}

	public void setTableLock(Map<String, String> tableLock) {
		this.tableLock = tableLock;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}

}
