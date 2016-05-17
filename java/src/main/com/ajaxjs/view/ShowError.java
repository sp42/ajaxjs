package com.ajaxjs.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

/**
 * 异常处理类
 */
public class ShowError {
	/**
	 * 收集错误信息
	 * 
	 * @param request
	 * @param ex
	 * @param out
	 */
	public ShowError(HttpServletRequest request, Throwable ex, JspWriter out) {
		// 全部内容先写到内存，然后分别从两个输出流再输出到页面和文件
		try(
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			PrintStream printStream = new PrintStream(byteArrayOutputStream);
		){
			printStream.println();
			printStream.println("用户账号：" + request.getSession().getAttribute("userName"));
			printStream.println("访问的路径: " + getInfo(request, "javax.servlet.forward.request_uri", String.class));
			printStream.println("出错页面地址: " + getInfo(request, "javax.servlet.error.request_uri", String.class));
			printStream.println("错误代码: " + getInfo(request, "javax.servlet.error.status_code", int.class));
			printStream.println("异常的类型: " + getInfo(request, "javax.servlet.error.exception_type", Class.class));
			printStream.println("异常的信息: " + getInfo(request, "javax.servlet.error.message", String.class));
			printStream.println("异常servlet: " + getInfo(request, "javax.servlet.error.servlet_name", String.class));
			printStream.println();

			// 另外两个对象
			getInfo(request, "javax.servlet.jspException", Throwable.class);
			getInfo(request, "javax.servlet.forward.jspException", Throwable.class);

			Map<String, String[]> map = request.getParameterMap();

			for (String key : map.keySet()) {
				printStream.println("请求中的 Parameter 包括：");
				printStream.println(key + "=" + request.getParameter(key));
				printStream.println();
			}

			for (Cookie cookie : request.getCookies()) { // cookie.getValue()
				printStream.println("请求中的 Cookie 包括：");
				printStream.println(cookie.getName() + "=" + cookie.getValue());
				printStream.println();
			}
			
			if (ex != null) {
				printStream.println("异常信息");
				printStream.println(ex.getClass() + " : " + ex.getMessage());
				printStream.println();

				printStream.println("堆栈信息");
				ex.printStackTrace(printStream);
				printStream.println();
			}
			
			if (out != null) {
				try {
					out.print(byteArrayOutputStream); // 输出到网页
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

//			log(request, byteArrayOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 记录异常信息
	 * @param request
	 */
	public void log(HttpServletRequest request, ByteArrayOutputStream byteArrayOutputStream) {
		File dir = new File(request.getSession().getServletContext().getRealPath("/errorLog"));
		if (!dir.exists()) {
			dir.mkdir();
		}

		String timeStamp = new java.text.SimpleDateFormat("yyyyMMddhhmmssS").format(new Date());
		File file = new File(dir.getAbsolutePath() + File.separatorChar + "error-" + timeStamp + ".txt");

		try (java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file);
			PrintStream ps = new PrintStream(fileOutputStream)) {// 写到文件
			ps.print(byteArrayOutputStream);
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param request
	 * @param key
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T getInfo(HttpServletRequest request, String key, Class<T> type) {
		Object obj = request.getAttribute(key);
		return obj == null ? null : (T) obj;
	}

	// /**
	// * 在页面上打印异常
	// *
	// * @param e
	// * @param out
	// */
	// public static void printErr2page(Throwable e, JspWriter out) {
	// PrintWriter pOut = new PrintWriter(out);
	// e.printStackTrace(pOut);
	// }
	//
	// /**
	// * 在页面上打印异常
	// *
	// * @param e
	// * @param response
	// */
	// public static void printErr2page(Throwable e, HttpServletResponse
	// response) {
	// try {
	// e.printStackTrace(response.getWriter());
	// } catch (IOException e1) {
	// }
	// }
}