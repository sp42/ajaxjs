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
package com.ajaxjs.util.stream;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.net.http.HttpClient.Request;
import com.ajaxjs.util.IO;

/**
 * Web 方面的流
 * @author frank
 *
 */
public class Web extends IO {
	
	/**
	 * 下载任意 web 文件到本地
	 * TODO 下载远程http地址的图片文件到本地-自动处理图片是否经过服务器gzip压缩的问题
     * http://blog.csdn.net/5iasp/article/details/8085907
	 * @param url
	 * @param filename
	 */
	public static void getRemote2local(String url, String filename){
		Request req = new Request(url);
		req.call();
		InputStream in = req.getBody();
		
		if ("gzip".equals(req.conn.getHeaderField("Content-Encoding"))) {
			try {
				save2file(filename, new GZIPInputStream(in));
				in.close(); // in没有传到 in2out，所以不会自动关闭
			} catch (IOException e) {
				e.printStackTrace();
			}	 
		} else {
			save2file(filename, in);	 
		}
	}
	
	/**
	 * web文件下载功能实现 
	 * @param dirname 目录名称
	 * @param filename 获得请求文件名  
	 * @param context
	 * @param response
	 */
	public static void download(String dirname, String filename, ServletContext context, HttpServletResponse response){  
        // 设置文件 MIME 类型  
        response.setContentType(context.getMimeType(filename));  
        // 设置 Content-Disposition  
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);  
        // 获取目标文件的绝对路径  
        String fullFileName = context.getRealPath(dirname + filename);  
          
        // 读取目标文件，通过 response 将目标文件写到客户端 
        OutputStream out = null;
        try {
        	out = response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        if(out != null) readFile_save2(fullFileName, out);
        
        try {
			// 加上下面代码,运行时才不会出现java.lang.IllegalStateException: getOutputStream() has already been called ..........等异常  
        	// jsp 需要加上
        	// out.clear();  
        	// out = pageContext.pushBody();  
			response.getOutputStream().flush();    
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    } 
	
	/**
	 * servlet生成html
	 * http://my.oschina.net/mengdejun/blog/9434
	 * TODO
	 * @param context
	 * @param req
	 * @param resp
	 */
	public static void servlet2html(ServletContext context, HttpServletRequest req, HttpServletResponse resp){
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
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(byteos, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
		} catch (ServletException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		printw.flush();
		
		try (FileOutputStream fileos = new FileOutputStream("/index_jsp.html", false);){
			// 把jsp输出的内容写到xxx.htm
			byteos.writeTo(fileos);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	/**
	 * 把图片流显示出来
	 * @param im
	 * @param response
	 */
	public static void loadImage(RenderedImage im, HttpServletResponse response) {
		try {
			ImageIO.write(im, "JPEG", response.getOutputStream());

/*			// 加上下面代码,运行时才不会出现java.lang.IllegalStateException: getOutputStream() has already been called ..........等异常
			response.getOutputStream().flush();
			response.getOutputStream().close();
			response.flushBuffer();*/

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
}
