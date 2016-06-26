package com.ajaxjs.mvc;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
//@WebFilter("/test")
public class MVC_Controller extends LiteMvcFilter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("1111111111111111111111");
		map("/test", MVC_Controller.class).dispatchResult("OK", "/index.jsp");
	}

	private String someProperty ="jhgjhjh";

	/**
	 * * Handles HTTP GET requests * * @return OK if everything runs smooth
	 */
	public String get() {
		return "OK";
	}

	/**
	 * Handles HTTP POST requests. Note that this method does not return
	 * anything. This is because we * decided to write the response content in
	 * the method as opposed to * using JSP or any other template.
	 */
	public void post(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.getWriter().write("Hello World! (Post)");
	}

	public String getSomeProperty() {
		return someProperty;
	}

	public void setSomeProperty(String someProperty) {
		this.someProperty = someProperty;
	}
}
