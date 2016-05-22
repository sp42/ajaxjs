package com.ajaxjs.web;

/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.Constant;
import com.ajaxjs.util.Util;

/**
 * 建立一个响应包装器
 * @author Frank Chueng
 */
public class Responser extends HttpServletResponseWrapper{
	/**
	 * 创建一个 ResponseHelper 对象
	 * 
	 * @param response
	 *            原生 response 对象
	 */
	public Responser(HttpServletResponse response) {
		super(response);
	}
	
	private HttpServletRequest request;

	/**
	 * 保存一个关联的请求对象
	 * 
	 * @param request
	 *            关联的请求对象
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 返回当前请求对象
	 * 
	 * @return 请求对象
	 */
	public HttpServletRequest getRequest() {
		return request;
	}
	
	/**
	 * 输出任何 字符串 内容（默认设置 UTF-8 编码）
	 * 
	 * @param output
	 *            要输出的内容
	 */
	public void output(String output) {
		PrintWriter writer = null;
		setCharacterEncoding(Constant.encoding_UTF8);

		try {
			writer = getWriter();
		} catch (IOException e) {}

		if (writer != null) writer.print(output);
	}
	
	/**
	 * 输出 HTML 内容
	 * 
	 * @param HTML
	 *            要输出的 HTML 内容
	 */
	public void outputHTML(String HTML) {
		setContentType("text/html");
		output(HTML);
	}
	
	/**
	 * 输出一个最简单的 html 页面（已包含 <html>、<body> 标签），并支持中文，强制 UTF-8 编码
	 * 
	 * @param html
	 *            要输出的 HTML 内容
	 */
	public void outputSimpleHTML(String html) {
		html = String.format("<html><meta charset=\"utf-8\" /><body>%s</body></html>", html);
		outputHTML(html);
	}
	
	/**
	 * 是否输出 JSONP
	 */
	public boolean isOutputJSONP = false;
	public String JSONP_Token = "callBack";
	
	/**
	 * shorthand for request.setAttribute("output", save_jsp_fileContent());
	 * resHelper.outputJSON(request);
	 * 
	 * @param output
	 *            要输出的内容
	 */
	public void outputJSON(String output) {
		setContentType(isOutputJSONP ? "application/javascript" : "application/json");
		if (isOutputJSONP && request != null)
			output = request.getParameter(JSONP_Token) + "(" + output + ");";

		output(output);
	}
	
	public void outputJSON(Map<String, ?> map) {
		outputJSON(com.ajaxjs.json.Json.stringify(map));
	}
	
	/**
	 * 上文执行 request.setAttribute("output", ""); 读取
	 * request.getAttribute("output")，打印 JSON
	 */
	public void outputJSON() {
		if (request.getAttribute("output") == null)
			throw new NullPointerException("没有执行 request.getAttribute(\"output\") 保存输出数据");
		else {
			Object output = request.getAttribute("output");
			if (output instanceof String) {
				outputJSON(output.toString());
			} else { // simple object
				outputJSON(com.ajaxjs.json.Json.stringify(output));
			}
		}
	}

	/**
	 * 为特定的操作最后输出结果，把结果的结构固定下来，有 isOk 和 msg 两个字段
	 * 如out.write("{\"isOk\":false, \"msg\":\" "+ msg + " \"}"); 可以传入一个 map
	 * 定义更多的字段
	 * 
	 * @param isOk
	 *            是否搞定？
	 * @param msg
	 *            交互用的提示信息
	 * @param hash
	 *            自定义结构，可以为 null。null的时候固定 isOk 和 msg 两个字段
	 */
	public void outputAction(boolean isOk, String msg, Map<String, Object> hash) {
		hash = hash == null ? new HashMap<String, Object>() : hash;
		hash.put("isOk", isOk);
		hash.put("msg", msg);

		String output = com.ajaxjs.json.Json.stringify(hash);
		outputJSON(output);
	}

	/**
	 * 为特定的操作最后输出结果，把结果的结构固定下来，有 isOk 和 msg 两个字段
	 * 
	 * @param isOk
	 *            是否搞定？
	 * @param msg
	 *            交互用的提示信息
	 */
	public void outputAction(boolean isOk, String msg) {
		outputAction(isOk, msg, null);
	}
	
	/**
	 * 接口输出内容
	 */
	public void outputAction() {
		if (getRequest() == null)
			throw new NullPointerException("未设置 request 请求对象！");

		Requester request = this.request instanceof Requester 
				? (Requester) this.request 
				: new Requester(this.request);
		
		if (request.hasError()) {
			outputAction(false, request.getError());
		} else {
			if (request.getAttribute("msg") == null) {
				throw new IllegalArgumentException("没有对 request.getAttribute(\"msg\") 设置参数");
			} else
				outputAction(true, Util.to_String(request.getAttribute("msg")));
		}
	}

	/**
	 * 让接口返回没有数据
	 * @return JSON，其中 total = 0
	 */
	public static String queryZero() {
		return com.ajaxjs.json.Json.stringify(new Object() {
			@SuppressWarnings("unused")
			public int total = 0;
			@SuppressWarnings("unused")
			public String msg = Constant.sql_query_zero;
		});
	}
	
	/**
	 * 输出“有符合条件的记录，但分页超过页数”的 JSON
	 * 
	 * @param _total
	 *            总数
	 * @return JSON
	 */
	public static String overPage(final int _total) {
		return com.ajaxjs.json.Json.stringify(new Object() {
			@SuppressWarnings("unused")
			public int total = _total;
			@SuppressWarnings("unused")
			public String msg = "有符合条件的记录，但分页超过页数";
		});
	}
	
	/**
	 * 返回到前一页并刷新
	 */
	public final static String returnJs_refresh = "window.location = document.referrer;";

	/**
	 * 新的输出，不要缓存
	 */
	public void noCache() {
		setHeader("Pragma", "No-cache");
		setHeader("Cache-Control", "no-cache");
		setDateHeader("Expires", 0);
	}

	/**
	 * 跳转至 view 层
	 * 
	 * @param path
	 *            JSP 路径
	 */
	public void sendRequestDispatcher(String path) {
		if (getRequest() == null)
			throw new NullPointerException("未设置 request 请求对象！");
		
		try {
			request.getRequestDispatcher(path).forward(request, this);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 跳转的异常页面
	 * 
	 * @param ex
	 *            异常对象
	 */
	public void gotoErrorPage(Exception ex) {
		if (getRequest() == null) throw new NullPointerException("未设置 request 请求对象！");

		// request.setAttribute("javax.servlet.jsp.jspException", ex); // 不能传这个
		// jspException 否则不能跳转
		request.setAttribute("javax.servlet.error.exception_type", ex);
		request.setAttribute("javax.servlet.error.message", ex.getMessage());
		
		sendRequestDispatcher("/public/error.jsp");
	}
}
