package com.ajaxjs.cms.pageEditor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.web.Requester;
import com.ajaxjs.web.Responser;

@org.springframework.stereotype.Controller
@RequestMapping(value = "/cms/PageEditor")
public class PageEditorController {
	private static final LogHelper LOGGER = LogHelper.getLog(PageEditorController.class);
	/**
	 * 加载页面
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "/common/pageEditor/loadIframe";
	}
	
//	public void write() {
//
//		if (request.hasRoute("save")) {
//			save_jsp_fileContent();
//			response.outputAction();
//		} else if (request.hasRoute("upload")) {
//			response.outputJSON(uploadFile());
//		}
//	}
	/**
	 * 系统配置界面
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		Requester rh = new Requester(request);
		Responser rsp = new Responser(response);
		
		if (rh.getRequestURI().contains("getImgList")) {
			String folder = rh.Mappath(rh.getParameter("folder"));
			rsp.outputJSON(Service.getImgList(folder));
			
			return null;
		} else {
			getIframe_basePath(rh);
			String fullFilePath = rh.Mappath(rh.getParameter("url"));// 转为绝对路径
				   fullFilePath = Service.getFullPathByRequestUrl(fullFilePath);
				   
			try {
				request.setAttribute("content", Service.read_jsp_fileContent(fullFilePath));
			} catch (IOException e) {
				request.setAttribute("content", e.getMessage());
				LOGGER.warning("读取页面标识错误！", e);
			} 
			
			return "/common/pageEditor/editor";
		}
		
	}
	
	/**
	 * 小心使用，改方法的权限很大！
	 * @param request
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		Requester requester = new Requester(request);
		String  path = requester.Mappath(requester.getParameter("url")),
				content = requester.getParameter("content");
				
		try {
			Service.save_jsp_fileContent(path, content);
		} catch (IOException e) {
			e.printStackTrace();
			
		}

		requester.setActionMsg("修改成功");
		Responser rsp = new Responser(response);
		rsp.setRequest(request);
		
		rsp.outputAction();
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
			for (int i = 0; i < (arr.length - 1); i++) newArr[i] = arr[i];

			basePath = request.getContextPath() + "/"; // 加上项目前缀，使其完整
			basePath += StringUtil.stringJoin(newArr, "/");
		} else
			basePath = request.getContextPath() + url;

		request.setAttribute("basePath", basePath);

		return basePath;
	}
}
