package com.ajaxjs.web.simple_admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Admin
 */
public class AdminMgrController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("utf-8");

		String action = request.getParameter("action");

		if (action != null) {
			switch (action) {
			case "workbench":
				request.getRequestDispatcher("/asset/common/jsp/simple_admin/workbench.jsp").include(request, response);
				break;
			case "logout":
				response.getWriter().append("已退出！");
				break;
			default:
				response.getWriter().append("无效 action！");
			}
		} else {
			request.getRequestDispatcher("/asset/common/jsp/simple_admin/index.jsp").include(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}