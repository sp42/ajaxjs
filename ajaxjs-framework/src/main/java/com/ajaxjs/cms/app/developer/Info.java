package com.ajaxjs.cms.app.developer;

public class Info {

	public Info(String tableName) {
		this.setTableName(tableName);
	}

	private String tableName;
	
	private String beanName ;

	/**
	 * 请求页面地址，如 /sqlDoc.jsp
	 */
	private String jsp;

	/**
	 * 保存地址，如 c:\\sp42\\d.htm
	 */
	private String saveTarget;

	public String getJsp() {
		return jsp;
	}

	public void setJsp(String jsp) {
		this.jsp = jsp;
	}

	public String getSaveTarget() {
		return saveTarget;
	}

	public void setSaveTarget(String saveTarget) {
		this.saveTarget = saveTarget;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
}
