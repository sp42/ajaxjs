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
package com.ajaxjs.pageEditor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.util.StringUtil;

/**
 * 
 * @author Frank
 *
 */
public class PageEditorController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("url") == null) {
			throw new NullPointerException("缺少必填参数 url！");
		}
		
		String path = request.getParameter("url");
		path = Mappath(path);
		path = PageEditorService.getFullPathByRequestUrl(path);
		
		request.setAttribute("contentBody", PageEditorService.read_jsp_fileContent(path));
		request.getRequestDispatcher("/asset/jsp/pageEditor/editor.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			if(request.getParameter("url") == null) {
				throw new NullPointerException("缺少必填参数 url！");
			}
			
			if(request.getParameter("contentBody") == null) {
				throw new NullPointerException("缺少必填参数 contentBody！");
			}
			
			String contentBody = StringUtil.toChinese(request.getParameter("contentBody")), path = Mappath(request.getParameter("url"));
			
			PageEditorService.save_jsp_fileContent(path, contentBody);
			
			response.getWriter().append("{\"isOk\":true}");
		}catch(Throwable e) {
			response.getWriter().append("{\"isOk\":false,\"msg\": " + e.getMessage() + "}");
		}
	}
	
	public static String getEditJSP(HttpServletRequest request) {
		String uri = request.getRequestURI().replaceAll("admin/\\w+", "index");
		uri = uri.replace(request.getContextPath(), "");
		return uri;
	}
	
	/**
	 * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
	 * 
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public String Mappath(String relativePath) {
		String absoluteAddress = getServletContext().getRealPath(relativePath); // 绝对地址
		
		if (absoluteAddress != null)
			absoluteAddress = absoluteAddress.replace('\\', '/');
		return absoluteAddress;
	}
}
