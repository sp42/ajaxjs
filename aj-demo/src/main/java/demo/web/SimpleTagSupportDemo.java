package demo.web;

import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class SimpleTagSupportDemo extends SimpleTagSupport {
	@Override // doTag()是空方法须重写
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().println("Hello World!");// 显示文字
	}
}