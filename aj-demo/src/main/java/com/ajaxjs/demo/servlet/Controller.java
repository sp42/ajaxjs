package com.ajaxjs.demo.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/ServletMVC")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Student student = new Student();
		student.setName("Mike");
		request.setAttribute("student", student);
		// 读取数据库数据，填充到 Student，传递到 jsp 页面…… 
		request.getRequestDispatcher("/WEB-INF/jsp/simpleServlet.jsp").forward(request, response);
	}
}
