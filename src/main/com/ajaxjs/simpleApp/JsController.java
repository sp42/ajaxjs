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
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.js.JavaScriptCompressor;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.web.Captcha;

/**
 *  JS all in one
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/js")
public class JsController implements IController {

	@GET
	public String get(HttpServletRequest req, HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();
		File folder = new File("C:\\project\\wyzx-pc\\WebContent\\ajaxjs-ui\\js\\widgets");
		
		boolean isCompress = "true".equals(req.getParameter("compress"));
		for (File file : folder.listFiles()) {
			if(file.isFile()) {
				String jsCode = new FileUtil().setFile(file).read().byteStream2stringStream().close().getContent();
				sb.append(isCompress ? JavaScriptCompressor.compress(jsCode) : jsCode);
			}
		}
		
		return "js::" + sb.toString();
	}

}