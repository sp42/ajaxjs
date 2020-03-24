package com.ajaxjs.cms.app.developer;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.util.ReflectUtil;

public class Info {

	public Info(String tableName, String saveFolder) {
		this.setTableName(tableName);
		this.saveFolder = saveFolder;
	}

	/**
	 * 模板保存位置
	 */
	private static final String tplSave = BaseController.admin("developer/code-generator");

	private String type;
	private String tableName;

	private String beanName;

	private String saveFolder;

	/**
	 * 请求页面地址，如 /sqlDoc.jsp
	 */
	public String getJsp() {
		return tplSave + "/" + type + ".jsp";
	}

	/**
	 * 保存地址，如 c:\\sp42\\d.htm
	 */
	public String getSaveTarget() {
		return saveFolder + "/" + type + "/" + getBeanName() + ReflectUtil.firstLetterUpper(type) + ".java";
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getBeanName() {
		return beanName == null ? ReflectUtil.firstLetterUpper(tableName) : beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getType() {
		return type;
	}

	public Info setType(String type) {
		this.type = type;
		return this;
	}
}
