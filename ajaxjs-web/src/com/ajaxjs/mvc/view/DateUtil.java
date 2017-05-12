package com.ajaxjs.mvc.view;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ajaxjs.util.DateTools;

public class DateUtil extends SimpleTagSupport {

	private Object value;
	private Date date;
	private String format;

	@Override
	public void doTag() throws JspException, IOException {
		if (date == null && value == null)
			return;
		
		Date dateValue = DateTools.Objet2Date(value);
		String str = "";
		
		if (format == null)
			str = DateTools.formatDateShorter(dateValue);
		else
			str = DateTools.formatDate(dateValue, format);
		
		getJspContext().getOut().write(str);
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setDate(Date date) {
		this.date = date;
		this.value = (Object) date;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
