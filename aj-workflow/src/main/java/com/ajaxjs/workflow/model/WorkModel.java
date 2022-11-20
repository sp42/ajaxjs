package com.ajaxjs.workflow.model;

/**
 * 工作元素，业务逻辑的
 * 
 */
public abstract class WorkModel extends NodeModel {
	private static final long serialVersionUID = 761102386160546149L;

	/**
	 * 表单
	 */
	private String form;

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}
}
