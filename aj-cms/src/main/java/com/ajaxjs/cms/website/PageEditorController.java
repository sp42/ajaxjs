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
package com.ajaxjs.cms.website;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.controller.MvcRequest;

/**
 * 默认的页面编辑器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin/website/page_editor")
public class PageEditorController extends BaseController<Object> {
	private static final LogHelper LOGGER = LogHelper.getLog(PageEditorController.class);

	/**
	 * 浏览页面，加载一个含有 iframe 元素的页面。 对该页面你可以传入 url 参数指定 root 地址，否则就是 ../../，即 ${empty
	 * param.url ? '../../' : param.url}
	 * 
	 * @return iframe 元素的页面模版地址
	 */
	@GET
	public String show() {
		LOGGER.info("浏览页面");
		return jsp("website/page-load-iframe");
	}

	/**
	 * 编辑页面，加载一个含有 HTMLEditor 编辑的页面
	 * 
	 * @param r 请求对象
	 * @return 含有 HTMLEditor 编辑页面地址
	 * @throws IOException
	 */
	@GET
	@Path("loadPage")
	public String loadPage(@NotNull @QueryParam("url") String url, MvcRequest r) throws IOException {
		String path = getUrl(r.mappath(url));
		LOGGER.info("编辑页面" + path);
		r.setAttribute("contentBody", readContent(path));

		return jsp("website/page-editor");
	}

	/**
	 * 保存编辑后的内容
	 * 
	 * @param r 请求对象，必填 url 和 contentBody 两个参数
	 * @return JSON 结果
	 * @throws IOException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String save(MvcRequest r, @NotNull @QueryParam("url") String url, @NotNull @QueryParam("contentBody") String contentBody) throws IOException {
		String path = getUrl(r.mappath(url)); // 真实的磁盘文件路径
		String content = FileHelper.openAsText(path), toDelContent = readContent(path);// 读取旧内容
		LOGGER.info("保存编辑后的内容" + path);

		if (toDelContent != null) {
			content = content.replace(toDelContent, contentBody);
			FileHelper.saveText(path, content);
		}

		return jsonOk("修改页面成功！");
	}

	/**
	 * 可编辑标识开始
	 */
	private final static String START_TOKEN = "<!-- Editable AREA|START -->";

	/**
	 * 可编辑标识结束
	 */
	private final static String END_TOKEN = "<!-- Editable AREA|END -->";

	/**
	 * 根据页面中可编辑区域之标识，取出来。
	 * 
	 * @param path 完整的 jsp 文件路径
	 * @return 可编辑内容
	 * @throws IOException
	 */
	public static String readContent(String path) throws IOException {
		String content = FileHelper.openAsText(path);
		int start = content.indexOf(START_TOKEN), end = content.indexOf(END_TOKEN);

		try {
			return content.substring(start + START_TOKEN.length(), end);
		} catch (StringIndexOutOfBoundsException e) {
			String msg = "页面文件" + path + "中没有标记可编辑区域之标识。请参考：" + START_TOKEN + "/" + END_TOKEN;
			throw new IOException(msg);
		}
	}

	/**
	 * 请求附带文件参数，将其转换真实的磁盘文件路径
	 * 
	 * @param url URL 提交过来的磁盘文件路径，可能未包含文件名或加了很多 url 参数
	 * @return 完整的磁盘文件路径
	 */
	private static String getUrl(String url) {
		if (url.indexOf(".jsp") == -1 && url.indexOf(".htm") == -1)
			url += "/index.jsp"; // 加上 扩展名

		if (url.indexOf("?") != -1) // 去掉 url 参数
			url = url.replaceAll("\\?.*$", "");

		return url;
	}

	/**
	 * 获取指定目录内的图片 目录有 folder 参数指定 TODO
	 * 
	 * @param folder 包含图片的目录
	 * @return 图片文件名列表
	 */
	public static String getImgList(String folder) {
		File dir = new File(folder);
		File[] files = dir.listFiles();

		List<String> json = new ArrayList<String>();

		if (files == null)
			return null;

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				continue;
			} else {
				final String strFileName = files[i].getName();
				if (strFileName.contains(".jpg") || strFileName.contains(".gif") || strFileName.contains(".png")) {

					// String el = JsonHelper.stringify_object(new Object() {
					// @SuppressWarnings("unused")
					// public Boolean isOk = true;
					// @SuppressWarnings("unused")
					// public String fileName = strFileName;
					// @SuppressWarnings("unused")
					// public String id = strFileName;
					// });

					// json.add(el);
				}
			}
		}

		return String.format(jsonTpl, String.join(",", json), json.size());
	}

	private static final String jsonTpl = "{\"result\" : [%s], \"total\" : %s}";

	@Override
	public IBaseService<Object> getService() {
		return null;
	}

}
