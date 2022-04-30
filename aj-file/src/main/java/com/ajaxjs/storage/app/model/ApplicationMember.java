package com.ajaxjs.storage.app.model;

/**
 * @TableName("ufs_application_member")
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ApplicationMember {

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private String id;

	private Application application;

	private String userId;

}
