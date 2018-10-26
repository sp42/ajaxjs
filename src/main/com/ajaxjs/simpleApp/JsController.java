/**
	 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.simpleApp;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.util.io.FileUtil;

/**
 * JS all in one
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/js")
public class JsController implements IController {
	static String ajaxjs = "C:\\project\\ajaxjs-web\\WebContent\\asset\\ajaxjs-ui";

	@GET
	public String get(HttpServletRequest req, HttpServletResponse response) {
		return "js::" + action(ajaxjs + "\\js\\widgets", "true".equals(req.getParameter("compress")));
	}

	/**
	 * 
	 * @param _folder
	 * @param isCompress
	 * @return
	 */
	public static String action(String _folder, boolean isCompress) {
		StringBuilder sb = new StringBuilder();
		File folder = new File(_folder);

		for (File file : folder.listFiles()) {
			if (file.isFile()) {
				String jsCode = new FileUtil().setFile(file).read().byteStream2stringStream().close().getContent();
				sb.append(isCompress ? JavaScriptCompressor.compress(jsCode) : jsCode);
			}
		}

		return sb.toString();
	}

	static String getOne(File file) {
		String jsCode = new FileUtil().setFile(file).read().byteStream2stringStream().close().getContent();
		return JavaScriptCompressor.compress(jsCode);
	}

	static String getOne(String file) {
		return getOne(new File(file));
	}

	public static void main(String[] args) {
		String target = ajaxjs + "\\output\\all.js";
		String js = getOne(ajaxjs + "\\js\\ajaxjs-base.js");

		js += action(ajaxjs + "\\js\\widgets", true);
//		js += getOne(ajaxjs + "\\js\\widgets\\admin\\admin.js");

		System.out.println(target);
		new FileUtil().setFilePath(target).setOverwrite(true).setContent(js).save().close();

	}

	/**
	 * 压缩 CSS 并将其保存到一个地方
	 * 
	 * @param type main|admin
	 * @param css CSS 代碼
	 * @param request 請求對象
	 * @return 是否操作成功的结果
	 */
	@POST
	public String doCSS(@QueryParam("type") String type, @QueryParam("css") String css, MvcRequest request) {
		try {
			String fullpath = request.mappath("/asset/css/" + type + ".css");
			FileUtil.save(fullpath, css);

			return "json::{\"isOk\":true}";
		} catch (Throwable e) {
			e.printStackTrace();
			return "json::{\"isOk\":false}";
		}

	}
}