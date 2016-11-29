package com.ajaxjs.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.util.json.JsonHelper;

/**
 * 灵活的、链式调用的输出响应内容
 * 
 * @author frank
 *
 */
public class Output extends HttpServletResponseWrapper {

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

	public void go() {
		if (getRedirect() != null) {
			redirect(getRedirect());
			return;
		}

		if (getOutput_Map() != null) { // Map 的话转变为 json 输出
			setJson(true).setOutput(JsonHelper.stringify(getOutput_Map()));
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

	public void go(HttpServletRequest request) {
		if (getTemplate() != null) {
			sendRequestDispatcher(request, getTemplate());
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

		try {
			writer = getWriter();
		} catch (IOException e) {
		}

		if (writer != null)
			writer.print(output);
	}

	public Output(HttpServletResponse request) {
		super(request);
	}

	public String getOutput() {
		return output;
	}

	public Output setOutput(String output) {
		this.output = output;
		return this;
	}

	public String getContent_Type() {
		return content_Type;
	}

	public Output setContent_Type(String content_Type) {
		this.content_Type = content_Type;
		return this;
	}

	public String getRedirect() {
		return redirect;
	}

	public Output setRedirect(String redirect) {
		this.redirect = redirect;
		return this;
	}

	public String getTemplate() {
		return template;
	}

	public Output setTemplate(String template) {
		this.template = template;
		return this;
	}

	public void redirect(String url) {
		try {
			sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 跳转至 view 层
	 * 
	 * @param path
	 *            JSP 路径
	 */
	public void sendRequestDispatcher(HttpServletRequest request, String path) {
		try {
			RequestDispatcher rd = request.getRequestDispatcher(path);
			if (rd != null)
				rd.forward(request, this);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isJson() {
		return json;
	}

	public Output setJson(boolean json) {
		this.json = json;
		return this;
	}

	public boolean isSimpleHTML() {
		return simpleHTML;
	}

	public Output setSimpleHTML(boolean simpleHTML) {
		this.simpleHTML = simpleHTML;
		return this;
	}

	public String getJsonpToken() {
		return jsonpToken;
	}

	public Output setJsonpToken(String jsonpToken) {
		this.jsonpToken = jsonpToken;
		return this;
	}

	public Map<String, ?> getOutput_Map() {
		return output_Map;
	}

	public Output setOutput_Map(Map<String, ?> output_Map) {
		this.output_Map = output_Map;
		return this;
	}

	public Object getOutput_Obj() {
		return output_Obj;
	}

	public Output setOutput_Obj(Object output_Obj) {
		this.output_Obj = output_Obj;
		return this;
	}
}
