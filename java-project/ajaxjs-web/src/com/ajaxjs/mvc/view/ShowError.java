package com.ajaxjs.mvc.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

/**
 * 异常处理类，既可以在 Servlet 中使用，也可以在 Jsp 中使用。
 */
public class ShowError {
	/**
	 * 收集错误信息 输出到网页
	 * 
	 * @param request
	 *            请求对象
	 */
	public static OutputStream getError(HttpServletRequest request) {
		try(
			OutputStream os = new ByteArrayOutputStream();// 创建一个空的字节流，保存错误信息
			PrintStream ps = new PrintStream(os);
		){
			ps.println();
			// 收集错误信息
			ps.println("用户账号：" + request.getSession().getAttribute("userName"));
			ps.println("错误代码: " +     request.getAttribute("javax.servlet.error.status_code")); 
			ps.println("异常 Servlet: " + request.getAttribute("javax.servlet.error.servlet_name"));
			ps.println("出错页面地址: " + request.getAttribute("javax.servlet.error.request_uri"));
			ps.println("访问的路径: " + 	 request.getAttribute("javax.servlet.forward.request_uri"));
			ps.println("异常的类型: " + getInfo(request, 	"javax.servlet.error.exception_type", Class.class));// 好像重来没有作用
			ps.println("异常的信息: " + getInfo(request, 	"javax.servlet.error.message", String.class)); 		// 好像重来没有作用
			ps.println();

			for (String key : request.getParameterMap().keySet()) {
				ps.println("请求中的 Parameter 包括：");
				ps.println(key + "=" + request.getParameter(key));
				ps.println();
			}
			
			for (Cookie cookie : request.getCookies()) {
				ps.println("请求中的 Cookie 包括：");
				ps.println(cookie.getName() + "=" + cookie.getValue());
				ps.println();
			}

			// javax.servlet.jspException 等于 JSP 里面的 exception 对象
			Throwable ex;
			ex = getInfo(request, "javax.servlet.jspException", Throwable.class);
			if(ex == null) ex = getInfo(request, "javax.servlet.forward.jspException", Throwable.class);
	
			if (ex != null) {
				ps.println("异常信息");
				ps.println(ex.getClass() + " : " + ex.getMessage());
				ps.println();

				ps.println("堆栈信息");
				ex.printStackTrace(ps);
				ps.println();
			}
			
//			log(request, os);
			
			return os;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 记录异常信息到磁盘
	 * 
	 * @param request
	 *            请求对象
	 */
	public void log(HttpServletRequest request, ByteArrayOutputStream os) {
		File dir = new File(request.getSession().getServletContext().getRealPath("/errorLog"));
		if (!dir.exists()) {
			dir.mkdir();
		}

		String timeStamp = new java.text.SimpleDateFormat("yyyyMMddhhmmssS").format(new Date());
		File file = new File(dir.getAbsolutePath() + File.separatorChar + "error-" + timeStamp + ".txt");

		try (OutputStream fos = new java.io.FileOutputStream(file);
			PrintStream ps = new PrintStream(fos)) {// 写到文件
			ps.print(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 类型转换
	 * 
	 * @param request
	 *            请求对象
	 * @param key
	 *            键
	 * @param type
	 *            类型
	 * @return 已转换类型的对象
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getInfo(HttpServletRequest request, String key, Class<T> type) {
		Object obj = request.getAttribute(key);
		return obj == null ? null : (T) obj;
	}

	/**
	 * 在页面上打印异常
	 *
	 * @param e
	 *            异常
	 * @param out
	 *            输出
	 */
	public static void printErr2page(Throwable e, JspWriter out) {
		e.printStackTrace(new PrintWriter(out));
	}

	/**
	 * 在页面上打印异常
	 *
	 * @param e
	 *            异常
	 * @param response
	 *            输出
	 */
	public static void printErr2page(Throwable e, HttpServletResponse response) {
		try {
			e.printStackTrace(response.getWriter());
		} catch (IOException e1) {
		}
	}
}