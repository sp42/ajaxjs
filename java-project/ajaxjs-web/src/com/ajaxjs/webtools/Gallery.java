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
package com.ajaxjs.webtools;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.web.Requester;

/**
 * /cms/gallery
 * @author frank
 *
 */
public class Gallery {
	public String doGet(HttpServletRequest request) {
		if (request.getParameter("getJs") != null) {
			return "";
		} else {
			request.setAttribute("imgs", getImgs(new Requester(request).Mappath("/images")));
			return "common/misc/gallery";
		}
	}
	
	/**
	 * 返回某个文件夹里面的所有文件
	 * 
	 * @param folderName
	 *            文件夹名称
	 * @return
	 */
	public static String[] getImgs(String folderName) {
		File file = new File(folderName);
		return file.isDirectory() ? file.list() : null;
	}
}
