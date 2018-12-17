package com.ajaxjs.cms.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseBean;

public class Feedback extends BaseModel implements IBaseBean {
	private static final long serialVersionUID = 8996839117086200226L;
	private long pid;
	private Integer status;
	private Integer createdByUser;
	private Integer deleted;
	private Integer catelog;
	private String email;
	private String phone;
	private String feedback;
	private String userName;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(Integer createByUser) {
		this.createdByUser = createByUser;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getCatelog() {
		return catelog;
	}

	public void setCatelog(Integer catelog) {
		this.catelog = catelog;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
