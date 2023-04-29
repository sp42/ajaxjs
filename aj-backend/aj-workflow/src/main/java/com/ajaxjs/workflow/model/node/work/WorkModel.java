package com.ajaxjs.workflow.model.node.work;

import com.ajaxjs.workflow.model.node.NodeModel;

/**
 * 工作元素，业务逻辑的
 */
public abstract class WorkModel extends NodeModel {
	private static final long serialVersionUID = 761102386160546149L;

	/**
	 * 来自
	 */
	private String form;

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}
}
