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
package com.ajaxjs.web;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.net.http.ConnectException;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.Request;
import com.ajaxjs.net.http.RequestClient;
import com.ajaxjs.util.FileUtil;
import com.ajaxjs.util.Img;
import com.ajaxjs.util.Util;
import com.ajaxjs.util.json.JsonHelper;

/**
 * 建立一个响应包装器
 * @author Frank Chueng
 */
public class Responser extends HttpServletResponseWrapper {
	/**
	 * 创建一个 Responser 对象
	 * 
	 * @param response
	 *            原生 response 对象
	 */
	public Responser(HttpServletResponse response) {
		super(response);
		setCharacterEncoding(StandardCharsets.UTF_8.toString());
	}

	/**
	 * 创建一个 Responser 对象
	 * 
	 * @param resp
	 *            原生 ServletResponse 对象
	 */
	public Responser(ServletResponse resp) {
		this((HttpServletResponse) resp);
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
		setCharacterEncoding(StandardCharsets.UTF_8.toString());

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
		outputJSON(JsonHelper.stringify(map));
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
				outputJSON(JsonHelper.stringify_object(output));
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

		String output = JsonHelper.stringify(hash);
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
	
	public static final String sql_query_zero = "此次查询没有符合条件的任何数据。";

	/**
	 * 让接口返回没有数据
	 * @return JSON，其中 total = 0
	 */
	public static String queryZero() {
		return JsonHelper.stringify_object(new Object() {
			@SuppressWarnings("unused")
			public int total = 0;
			@SuppressWarnings("unused")
			public String msg = sql_query_zero;
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
		return JsonHelper.stringify_object(new Object() {
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
			RequestDispatcher rd = request.getRequestDispatcher(path);
			if(rd != null)rd.forward(request, this);
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
	
	/**
	 * web文件下载功能实现
	 * @param url
	 * @return
	 */
	public boolean download(String url, boolean isShowOnBrowser) {
		String filename = FileUtil.getFileName(url);
		
		ServletContext context = getRequest().getServletContext();
		setContentType(context.getMimeType(filename));// 设置文件 MIME 类型
		
		if(!isShowOnBrowser)
			setHeader("Content-Disposition", "attachment;filename=" + filename);// 设置 Content-Disposition
		
		final OutputStream out;
		
		try {
			out = getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if(url.startsWith("http://")) { // 远程网络资源
			Request req = new Request();
			req.setUrl(url);
			final RequestClient rc = new RequestClient(req);
			
			req.setCallback(new Request.Callback() {
				@Override
				public void onDataLoad(InputStream is) {
					try {
						FileUtil.write(is, out, true);
					} catch (IOException e) {
						e.printStackTrace();
					}// 直接写浏览器
				}
			});

			try {
				rc.connect();
			} catch (ConnectException e) {
				System.err.println(e);
				return false;
			}
		} else { 
			try (InputStream is = new FileInputStream(url);){
				FileUtil.write(is, out, true);// 文件在服务器的磁盘上，读取目标文件，通过 response 将目标文件写到浏览器
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}

		try {
			// jsp 需要加上下面代码,运行时才不会出现 java.lang.IllegalStateException: getOutputStream() has already been called ..........等异常
			// out.clear();
			// out = pageContext.pushBody();
			out.flush();
			out.close();
			flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * 把图片流显示出来
	 * 
	 * @param im
	 *            已渲染的图片对象
	 */
	public void loadImage(RenderedImage im) {
		try {
			ImageIO.write(im, "JPEG", getOutputStream());
	
			/*
			 * 加上下面代码,运行时才不会出现java.lang.IllegalStateException: getOutputStream() has already been called ..........等异常
			 * response.getOutputStream().flush();
			 * response.getOutputStream().close(); 
			 * response.flushBuffer();
			 */
	
			// JSP内置对象out和response.getWrite()的区别，两者的主要区别：1. 这两个对象的类型是完全不同的……
			// response.getWriter();
			// http://blog.sina.com.cn/s/blog_7217e4320101l8gq.html
			// http://www.2cto.com/kf/201109/103284.html
	
			// pageContext.getOut().clear();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO 跳转
		}
	}
	
	/**
	 * servlet生成
	 * html http://my.oschina.net/mengdejun/blog/9434 TODO
	 * 
	 * @param context
	 * @param req
	 * @param resp
	 */
	public static void servlet2html(ServletContext context, HttpServletRequest req, HttpServletResponse resp) {
		RequestDispatcher dispatcher = context.getRequestDispatcher("/index.jsp");

		final ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		final ServletOutputStream stream = new ServletOutputStream() {
			// 只是处理字节流，而PrintWriter则是处理字符流,和
			public void write(byte[] data, int offset, int length) {
				byteos.write(data, offset, length);
			}

			public void write(int b) throws IOException {
				byteos.write(b);
			}
		};
		OutputStreamWriter osw = new OutputStreamWriter(byteos, StandardCharsets.UTF_8);
		
		final PrintWriter printw = new PrintWriter(osw);

		// 进行编码转换,当输出流从比特流转换为字符流的时候设置才是有效的。
		HttpServletResponse rep = new HttpServletResponseWrapper(resp) {
			@Override
			public ServletOutputStream getOutputStream() {
				return stream;
			}

			@Override
			public PrintWriter getWriter() {
				return printw;
			}
		};

		try {
			dispatcher.include(req, rep);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		printw.flush();

		try (FileOutputStream fileos = new FileOutputStream("/index_jsp.html", false);) {
			byteos.writeTo(fileos);// 把 jsp 输出的内容写到 xxx.htm
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 图片服务器
	 * 
	 *  * <%@page pageEncoding="utf-8" import="com.ajaxjs.util.stream.Web" contentType="image/jpeg"%>
<%
Web.getImg("http://desk.fd.zol-img.com.cn/t_s1920x1200c5/g5/M00/0F/04/ChMkJ1dGqWOITU3xAA5PBuQ0iRMAAR6mAAAAAAADk8e103.jpg", request, response);
//清除输出流，防止释放时被捕获异常
out.clear();
out = pageContext.pushBody(); 
	 * 
	 * @param url
	 *            图片地址
	 * @throws IOException
	 */
	public void getImg(String url) throws IOException {
		long imgSize = Get.getFileSize(url);
		
		if (imgSize < (1024 * 100)) {
			sendRedirect(url);// 发送重定向
			return;
		} else {
			String imgType = getImgType(url);
			setContentType(getContentType(imgType));
			
			try (
				InputStream is = new URL(url).openStream();
				ServletOutputStream op = getOutputStream();
			){
				String height = request.getParameter("h"), width = request.getParameter("w");
				
				if (height != null && width != null) {
					BufferedImage bufImg = Img.setResize(ImageIO.read(is), Integer.parseInt(height), Integer.parseInt(width));
					ImageIO.write(bufImg, imgType, op);
				} else if (height != null) {
					Image img = ImageIO.read(is);// 将输入流转换为图片对象
					int[] size = Img.resize(img, Integer.parseInt(height), 0);
					
					BufferedImage bufImg = Img.setResize(img, size[0], size[1]);
					ImageIO.write(bufImg, imgType, op);
				} else if (width != null) {
					Image img = ImageIO.read(is);// 将输入流转换为图片对象
					int[] size = Img.resize(img, 0, Integer.parseInt(width));
					
					BufferedImage bufImg = Img.setResize(img, size[0], size[1]);
					ImageIO.write(bufImg, imgType, op);
				} else {
					FileUtil.write(is, op, true);// 直接写浏览器
				} 
			}  
		}
	}
	
	/**
	 * 返回 Content type
	 * @param imgType
	 * @return
	 */
	private static String getContentType(String imgType) {
		switch (imgType) {
		case "jpg":
			return "image/jpeg";
		case "gif":
			return "image/gif";
		case "png":
			return "image/png";
		default:
			return null;
		}
	}

	/**
	 * 获取 url 最后的 .jpg/.png/.gif
	 * @param url
	 * @return
	 */
	private static String getImgType(String url) {
		String[] arr = url.split("/");
		arr = arr[arr.length - 1].split("\\.");
		String t = arr[1];
		
		return t.replace('.', ' ').trim().toLowerCase();
	}
}
