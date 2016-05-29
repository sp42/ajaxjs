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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.IO;

/**
 * Web 方面的流
 * 
 * <%@page pageEncoding="utf-8" import="com.ajaxjs.util.stream.Web" contentType="image/jpeg"%>
<%
Web.getImg("http://desk.fd.zol-img.com.cn/t_s1920x1200c5/g5/M00/0F/04/ChMkJ1dGqWOITU3xAA5PBuQ0iRMAAR6mAAAAAAADk8e103.jpg", request, response);
//清除输出流，防止释放时被捕获异常
out.clear();
out = pageContext.pushBody(); 
%>
 * 
 * @author frank
 *
 */
public class Web extends IO {

	public static void getImg(String url, HttpServletRequest request,  HttpServletResponse response) throws IOException {
		System.out.println("请求地址：" + url);
		long imgSize = Get.getFileSize(url);
		if (imgSize < (1024 * 100)) {
			response.sendRedirect(url);// 发送重定向
			return;
		} else {
//			System.out.println("BigImg");
			
			String imgType = getImgType(url);
			
			response.setContentType(getContentType(imgType));
			try (
				InputStream is = new URL(url).openStream();
				ServletOutputStream op = response.getOutputStream();
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
					// 直接写浏览器
					byte[] buf = new byte[1024];
					int len = is.read(buf, 0, 1024);
					while (len != -1) {
						op.write(buf, 0, len);
						len = is.read(buf, 0, 1024);
					}
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
