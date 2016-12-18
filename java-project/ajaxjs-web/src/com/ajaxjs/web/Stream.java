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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.net.http.Client;
import com.ajaxjs.net.http.ConnectException;
import com.ajaxjs.net.http.Request;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.io.ImageUtil;
import com.ajaxjs.util.io.StreamUtil;

/**
 * @author Frank Chueng
 */
public class Stream  extends HttpServletResponseWrapper{
	public Stream(HttpServletResponse response) {
		super(response);
	}

	/**
	 * 输出“有符合条件的记录，但分页超过页数”的 JSON
	 * 
	 * @param _total
	 *            总数
	 * @return JSON
	 */

	/**
	 * web文件下载功能实现
	 * @param url
	 * @return
	 */
	public boolean download(HttpServletRequest request, String url, boolean isShowOnBrowser) {
		String filename = FileUtil.getFileName(url);
		
		ServletContext context = request.getServletContext();
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
			// 文件在服务器的磁盘上，读取目标文件，通过 response 将目标文件写到浏览器
			new StreamUtil().setIn(new FileInputStream(url)).setOut(out).write(true).close();
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
			@Override
			// 只是处理字节流，而PrintWriter则是处理字符流,和
			public void write(byte[] data, int offset, int length) {
				byteos.write(data, offset, length);
			}
			
			@Override
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
	public void getImg(HttpServletRequest request, String url) throws IOException {
		long imgSize = Client.getFileSize(url);
		
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
				
//				if (height != null && width != null) {
//					BufferedImage bufImg = ImageUtil.setResize(ImageIO.read(is), Integer.parseInt(height), Integer.parseInt(width));
//					ImageIO.write(bufImg, imgType, op);
//				} else if (height != null) {
//					Image img = ImageIO.read(is);// 将输入流转换为图片对象
//					int[] size = Img.resize(img, Integer.parseInt(height), 0);
//					
//					BufferedImage bufImg = Img.setResize(img, size[0], size[1]);
//					ImageIO.write(bufImg, imgType, op);
//				} else if (width != null) {
//					Image img = ImageIO.read(is);// 将输入流转换为图片对象
//					int[] size = Img.resize(img, 0, Integer.parseInt(width));
//					
//					BufferedImage bufImg = Img.setResize(img, size[0], size[1]);
//					ImageIO.write(bufImg, imgType, op);
//				} else {
//					FileUtil.write(is, op, true);// 直接写浏览器
//				} 
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
