package com.ajaxjs.app.service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ajaxjs.framework.BaseModel;

public class Feedback extends BaseModel {
	private static final long serialVersionUID = 4609146659455376973L;

	@NotBlank(message = "联系方式不能为空")
	@Size(min = 2, max = 255, message = "长度应该介于3和255之间")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank(message = "留言内容不能为空")
	@Size(min = 10, max = 255, message = "留言内容长度应该介于10和255之间的字符")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
