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
package com.ajaxjs.mvc.controller;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.PageContext;

import com.ajaxjs.js.JsonHelper;

/**
 * 灵活的、链式调用的输出响应内容
 * 
 * @author frank
 *
 */
public class MvcOutput extends HttpServletResponseWrapper {
	/**
	 * 创建一个 Output 对象
	 * 
	 * @param request
	 *            请求对象
	 */
	public MvcOutput(HttpServletResponse request) {
		super(request);
	}

	/**
	 * 创建一个 Output 对象
	 * 
	 * @param resp
	 *            原生 ServletResponse 对象
	 */
	public MvcOutput(ServletResponse resp) {
		this((HttpServletResponse) resp);
	}

	/**
	 * 最终输出的字符串，可以是特定的内容
	 */
	private String output;

	/**
	 * 输入内容可以是个 map
	 */
	private Map<String, ?> output_Map;

	/**
	 * 输入内容甚至可以是个 object
	 */
	private Object output_Obj;

	/**
	 * 输出类型，默认为网页="text/html"
	 */
	private String content_Type = "text/html";

	/**
	 * 302 重定向跳转
	 */
	private String redirect;

	/**
	 * JSP 模板路径
	 */
	private String template;

	/**
	 * 是否输出 json
	 */
	private boolean json;

	/**
	 * 是否 JSONP
	 */
	private String jsonpToken;

	/**
	 * 输出一个最简单的 html 页面（已包含 <html>、<body> 标签），并支持中文，强制 UTF-8 编码
	 */
	private boolean simpleHTML;

	/**
	 * 执行输出
	 */
	public void go() {
		if (getRedirect() != null) {
			try {
				sendRedirect(getRedirect()); // 302 重定向
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (getOutput_Map() != null) { // Map 的话转变为 json 输出
			setJson(true).setOutput(JsonHelper.stringifyMap(getOutput_Map()));
		} else if (getOutput_Obj() != null) {// map or object 二选其一
			setJson(true).setOutput(JsonHelper.stringify_object(getOutput_Obj()));
		}

		if (isJson()) {
			setContent_Type("application/json");
		} else if (getJsonpToken() != null) {
			setContent_Type("application/javascript");
			setOutput(String.format("%s(%s);", getJsonpToken(), getOutput()));
		} else if (isSimpleHTML()) {
			setOutput(String.format("<html><meta charset=\"utf-8\" /><body>%s</body></html>", getOutput()));
		}

		output(getOutput());
	}

	/**
	 * MVC 的 View 输出
	 * 
	 * @param request
	 *            请求对象
	 */
	public void go(HttpServletRequest request) {
		if (getTemplate() != null) {
			try {
				RequestDispatcher rd = request.getRequestDispatcher(getTemplate());// JSP 路径
				
				if (rd != null)
					rd.forward(request, this); // 跳转至 view 层
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 把图片流显示出来
	 * 
	 * @param im
	 *            已渲染的图片对象
	 */
	public void go(RenderedImage im) {
		if (getContent_Type() != null)
			setContentType(getContent_Type());

		try {
			ImageIO.write(im, "JPEG", getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * JSP 需要加上下面代码,运行时才不会出现 java.lang.IllegalStateException: getOutputStream()
	 * has already been called ..........等异常
	 * 
	 * JSP内置对象out和response.getWrite()的区别
	 * http://blog.sina.com.cn/s/blog_7217e4320101l8gq.html
	 * http://www.2cto.com/kf/201109/103284.html
	 * 
	 * @param response
	 */
	public static void fix(PageContext pageContext) {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		try {
			OutputStream out = response.getOutputStream();
			out.flush();
			out.close();
			response.flushBuffer();

			pageContext.getOut().clear();
			pageContext.pushBody();
			// out = pageContext.pushBody();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出任何 字符串 内容（默认设置 UTF-8 编码）
	 * 
	 * @param output
	 *            要输出的内容
	 */
	public void output(String output) {
		PrintWriter writer = null;
		setCharacterEncoding(StandardCharsets.UTF_8.toString());
		setContentType(getContent_Type());

		try {
			writer = getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (writer != null)
			writer.print(output);
	}

	/**
	 * 新的输出，不要缓存
	 */
	public MvcOutput noCache() {
		setHeader("Pragma", "No-cache");
		setHeader("Cache-Control", "no-cache");
		setDateHeader("Expires", 0);

		return this;
	}

	/**
	 * 返回到前一页并刷新
	 */
	public final static String returnJs_refresh = "window.location = document.referrer;";

	public String getOutput() {
		return output;
	}

	public MvcOutput setOutput(String output) {
		this.output = output;
		return this;
	}

	public String getContent_Type() {
		return content_Type;
	}

	public MvcOutput setContent_Type(String content_Type) {
		this.content_Type = content_Type;
		return this;
	}

	public String getRedirect() {
		return redirect;
	}

	public MvcOutput setRedirect(String redirect) {
		this.redirect = redirect;
		return this;
	}

	public String getTemplate() {
		return template;
	}

	public MvcOutput setTemplate(String template) {
		this.template = template;
		return this;
	}

	public boolean isJson() {
		return json;
	}

	public MvcOutput setJson(boolean json) {
		this.json = json;
		return this;
	}

	public boolean isSimpleHTML() {
		return simpleHTML;
	}

	public MvcOutput setSimpleHTML(boolean simpleHTML) {
		this.simpleHTML = simpleHTML;
		return this;
	}

	public String getJsonpToken() {
		return jsonpToken;
	}

	public MvcOutput setJsonpToken(String jsonpToken) {
		this.jsonpToken = jsonpToken;
		return this;
	}

	public Map<String, ?> getOutput_Map() {
		return output_Map;
	}

	public MvcOutput setOutput_Map(Map<String, ?> output_Map) {
		this.output_Map = output_Map;
		return this;
	}

	public Object getOutput_Obj() {
		return output_Obj;
	}

	public MvcOutput setOutput_Obj(Object output_Obj) {
		this.output_Obj = output_Obj;
		return this;
	}
}
