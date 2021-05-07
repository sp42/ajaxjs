package com.ajaxjs.web.view;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * 输入小数类型，取整数
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ToFix extends SimpleTagSupport {
	/**
	 * 输入的值，可以是任意类型，要经过转换。
	 */
	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void doTag() throws JspException, IOException {
		if (value == null)
			return;

		String format = null;

		if (value instanceof Float)
			format = Math.round((float) value) + "";
		if (value instanceof Double)
			format = Math.round((double) value) + "";
		else
			format = value + "";

		getJspContext().getOut().write(format);
	}
}