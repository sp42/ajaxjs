package com.ajaxjs.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Servlet implementation class Render
 */
@WebServlet("/Render")
public class Render extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Render() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/pojo.jsp", save = "c:\\sp42\\pojo.java";
		ServletContext sc = getServletContext();
		render(url, save, sc, request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * 
	 * @param url
	 *            请求页面地址，如 /sqlDoc.jsp
	 * @param save
	 *            保存地址，如 c:\\sp42\\d.htm
	 * @param sc
	 * @param request
	 * @param response
	 */
	private static void render(String url, String save, ServletContext sc, HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher rd = sc.getRequestDispatcher(url);

		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final ServletOutputStream stream = new ServletOutputStream() {
			@Override
			public void write(byte[] data, int offset, int length) {
				os.write(data, offset, length);
			}

			@Override
			public void write(int b) throws IOException {
				os.write(b);
			}
		};

		try {
			// 此处需要转码，防止中文字符乱码
			final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));

			HttpServletResponse rep = new HttpServletResponseWrapper(response) {
				@Override
				public ServletOutputStream getOutputStream() {
					return stream;
				}

				@Override
				public PrintWriter getWriter() {
					return pw;
				}
			};

			rep.setCharacterEncoding("utf-8");// response的编码为gbk防乱码
			rd.include(request, rep);
			pw.flush();

			FileOutputStream fos = new FileOutputStream(save);
			os.writeTo(fos);
			

			os.close();
			stream.close();
			pw.close();
			fos.close();
		} catch (IOException | ServletException e) {
			e.printStackTrace();
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
