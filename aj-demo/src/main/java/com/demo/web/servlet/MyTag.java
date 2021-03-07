package com.demo.web.servlet;

import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class MyTag extends SimpleTagSupport {
	// doTag() 是空方法，必须重写
	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().println("Hello World!");// 显示文字
	}
}
