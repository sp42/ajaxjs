package com.demo.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends HttpServlet{

	private static final long serialVersionUID = -6093790838998499817L;
	
	// GET 请求都会到这方法里面来
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("处理GET请求……");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("<html>");  // 静态内容
		out.println("<body>");  // 静态内容
		out.println("当前日期是" + new Date().toString());  // 动态内容
		out.println("</body>"); // 静态内容 
		out.println("</html>"); // 静态内容
	}
}
