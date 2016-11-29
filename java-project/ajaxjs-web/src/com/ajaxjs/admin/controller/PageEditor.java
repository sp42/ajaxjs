package com.ajaxjs.admin.controller;

import java.io.IOException;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.admin.service.PageEditorService;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.web.Requester;
import com.ajaxjs.web.Responser;

@Controller
@Path("/admin/PageEditor")
public class PageEditor implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(PageEditor.class);
	
	private static final String perfix = "/WEB-INF/jsp/common/admin/PageEditor";

	/**
	 * 加载页面
	 * 
	 * @return
	 */
	@GET
	@Path("/index")
	public String index() {
		return perfix + "/loadIframe.jsp";
	}

	// public void write() {
	//
	// if (request.hasRoute("save")) {
	// save_jsp_fileContent();
	// response.outputAction();
	// } else if (request.hasRoute("upload")) {
	// response.outputJSON(uploadFile());
	// }
	// }
	
	/**
	 * 系统配置界面
	 * 
	 * @return
	 */
	@GET
	public String doGet(Requester request, Responser response) {
		if (request.getRequestURI().contains("getImgList")) {
			String folder = request.Mappath(request.getParameter("folder"));
			response.outputJSON(PageEditorService.getImgList(folder));

			return null;
		} else {
			getIframe_basePath(request);
			String fullFilePath = request.Mappath(request.getParameter("url"));// 转为绝对路径
			fullFilePath = PageEditorService.getFullPathByRequestUrl(fullFilePath);

			try {
				request.setAttribute("content", PageEditorService.read_jsp_fileContent(fullFilePath));
			} catch (IOException e) {
				request.setAttribute("content", e.getMessage());
				LOGGER.warning("读取页面标识错误！", e);
			}

			return perfix + "/editor.jsp";
		}

	}

	/**
	 * 小心使用，改方法的权限很大！
	 * 
	 * @param request
	 * @param response
	 */
	@POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		Requester requester = new Requester(request);
		String path = requester.Mappath(requester.getParameter("url")), content = requester.getParameter("content");

		try {
			PageEditorService.save_jsp_fileContent(path, content);
		} catch (IOException e) {
			e.printStackTrace();

		}

		requester.setActionMsg("修改成功");
	}

	/**
	 * 解析 url 参数，返回 iframe base tag 所用的地址
	 * 
	 * @param Requester
	 * @return
	 */
	public static String getIframe_basePath(HttpServletRequest request) {
		String basePath = null;
		// 页面的所在位置，参数不用项目前缀
		String url = request.getParameter("url");

		// 只需要目录，不需要文件名，但传如来的是包含文件名的，所以要截取掉
		if (url.contains(".jsp") || url.contains(".htm")) {
			String[] arr = url.split("/"), newArr = new String[arr.length - 1];
			for (int i = 0; i < (arr.length - 1); i++)
				newArr[i] = arr[i];

			basePath = request.getContextPath() + "/"; // 加上项目前缀，使其完整
			basePath += StringUtil.stringJoin(newArr, "/");
		} else
			basePath = request.getContextPath() + url;

		request.setAttribute("basePath", basePath);

		return basePath;
	}
}
