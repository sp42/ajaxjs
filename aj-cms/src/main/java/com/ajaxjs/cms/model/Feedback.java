package com.ajaxjs.cms.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ajaxjs.framework.BaseModel;

/**
 * 留言反馈
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Feedback extends BaseModel {
	private static final long serialVersionUID = 4609146659455376973L;

	@NotBlank(message = "名字不能为空")
	@Size(min = 2, max = 255, message = "长度应该介于3和255之间")
	private String name;

	private Long userId;

	private String phone;
	
	@Email
	private String email;

	private String feedback;

	private String contact;

	@NotBlank(message = "留言内容不能为空")
	@Size(min = 10, max = 255, message = "留言内容长度应该介于10和255之间的字符")
	private String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
