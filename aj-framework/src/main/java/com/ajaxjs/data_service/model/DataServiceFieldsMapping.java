package com.ajaxjs.data_service.model;

/**
 * 不同字段的映射
 */
public class DataServiceFieldsMapping {
	private String id;
	private String createDate;
	private String createUser;
	private String updateDate;
	private String updateUser;
	private String delStatus;
	private Boolean dbStyle2CamelCase;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public Boolean getDbStyle2CamelCase() {
		return dbStyle2CamelCase;
	}

	public void setDbStyle2CamelCase(Boolean dbStyle2CamelCase) {
		this.dbStyle2CamelCase = dbStyle2CamelCase;
	}
}
