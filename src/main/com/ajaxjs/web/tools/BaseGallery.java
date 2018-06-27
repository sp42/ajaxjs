/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.web.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.web.WebUtil;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Controller
@Path("/admin/gallery")
public class BaseGallery implements IController {
	@GET
	public String gallery(HttpServletRequest request) {
		String folder = WebUtil.Mappath(request, "/images");

		File[] files = new File(folder).listFiles();
		if (files == null)
			return null;

		List<String> filenames = new ArrayList<>();

		for (File file : files) {
			if (!file.isDirectory()) {
				String fileName = file.getName();
				if (fileName.contains(".jpg") || fileName.contains(".gif") || fileName.contains(".png"))
					filenames.add("\"" + fileName + "\"");
			}
		}

		String json = "json::[" + StringUtil.stringJoin(filenames, ",") + "]";
		return json;
	}
}
