package com.ajaxjs.web.widget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.HtmlHead;

@Controller
@Path("/admin/page_editor")
public class PageEditor implements IController, Constant {
	private static final LogHelper LOGGER = LogHelper.getLog(PageEditor.class);

	@GET
	public String show() {
		return common_jsp_perfix + "pageEditor/loadIframe.jsp";
	}

	@GET
	@Path("loadPage")
	public String loadPage(HttpServletRequest request) {
		if (request.getParameter("url") == null)
			throw new NullPointerException("缺少必填参数 url！");

		String path = request.getParameter("url");

		try {
			path = getFullPathByRequestUrl(HtmlHead.Mappath(request, path));
			request.setAttribute("contentBody", read_jsp_fileContent(path));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return common_jsp_perfix + "pageEditor/editor.jsp";
	}

	@POST
	public String save(HttpServletRequest request) {
		try {
			if (request.getParameter("url") == null)
				throw new NullPointerException("缺少必填参数 url！");

			if (request.getParameter("contentBody") == null)
				throw new NullPointerException("缺少必填参数 contentBody！");

			String contentBody = request.getParameter("contentBody"), path = HtmlHead.Mappath(request, request.getParameter("url"));

			save_jsp_fileContent(path, contentBody);

			return String.format(json_ok, "修改页面成功！");
		} catch (Throwable e) {
			return String.format(json_not_ok, e.toString());
		}
	}

	/**
	 * 可编辑标识开始
	 */
	private final static String startToken = "<!-- Editable AREA|START -->";

	/**
	 * 可编辑标识结束
	 */
	private final static String endToken = "<!-- Editable AREA|END -->";

	/**
	 * 根据 页面中可编辑区域之标识，取出来。
	 * 
	 * @param fullFilePath
	 *            完整的 jsp 文件路径
	 * @return 可编辑内容
	 * @throws IOException
	 */
	public static String read_jsp_fileContent(String fullFilePath) throws IOException {
		String jsp_fileContent = FileUtil.openAsText(fullFilePath);

		int start = jsp_fileContent.indexOf(startToken), end = jsp_fileContent.indexOf(endToken);

		try {
			jsp_fileContent = jsp_fileContent.substring(start + startToken.length(), end);
		} catch (StringIndexOutOfBoundsException e) {
			jsp_fileContent = null;

			String msg = "页面文件" + fullFilePath + "中没有标记可编辑区域之标识。请参考：" + startToken + "/" + endToken;
			LOGGER.info(msg);
			throw new IOException(msg);
		}

		return jsp_fileContent;
	}

	/**
	 * 请求附带文件参数，将其转换真实的磁盘文件路径
	 * 
	 * @param rawFullFilePath
	 *            URL 提交过来的磁盘文件路径，可能未包含文件名或加了很多 url 参数
	 * @return 完整的磁盘文件路径
	 */
	public static String getFullPathByRequestUrl(String rawFullFilePath) {
		if (rawFullFilePath.indexOf(".jsp") == -1 && rawFullFilePath.indexOf(".htm") == -1)
			rawFullFilePath += "/index.jsp"; // 加上 扩展名

		if (rawFullFilePath.indexOf("?") != -1) // 去掉 url 参数
			rawFullFilePath = rawFullFilePath.replaceAll("\\?.*$", "");

		return rawFullFilePath;
	}

	/**
	 * 保存要修改的页面
	 * 
	 * @param rawFullFilePath
	 *            真实的磁盘文件路径
	 * @param newContent
	 *            新提交的内容
	 * @throws IOException
	 */
	public static void save_jsp_fileContent(String rawFullFilePath, String newContent) throws IOException {
		String fullFilePath = getFullPathByRequestUrl(rawFullFilePath); // 真实的磁盘文件路径
		String jsp_fileContent = FileUtil.openAsText(fullFilePath), toDel_fileContent = read_jsp_fileContent(fullFilePath);// 读取旧内容

		if (toDel_fileContent != null) {
			jsp_fileContent = jsp_fileContent.replace(toDel_fileContent, newContent);
			new FileUtil().setFilePath(fullFilePath).setContent(jsp_fileContent).save(); // 保存新内容
		} else {
			throw new IOException("页面文件中没有标记可编辑区域之标识。请参考： startToken/endTpoken");
		}
	}

	/**
	 * 获取指定目录内的图片 目录有 folder 参数指定
	 * 
	 * @param folder
	 *            包含图片的目录
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

					//					String el = JsonHelper.stringify_object(new Object() {
					//						@SuppressWarnings("unused")
					//						public Boolean isOk = true;
					//						@SuppressWarnings("unused")
					//						public String fileName = strFileName;
					//						@SuppressWarnings("unused")
					//						public String id = strFileName;
					//					});

					//					json.add(el);
				}
			}
		}

		return String.format(jsonTpl, StringUtil.stringJoin(json, ","), json.size());
	}

	private static final String jsonTpl = "{\"result\" : [%s], \"total\" : %s}";

	/**
	 * 删除文件（小心！）
	 * 
	 * @param file
	 *            rh.Mappath(rh.get("file")
	 * @return 是否删除成功
	 */
	public static boolean delFile(String file) {
		return new File(file).delete();
	}

	public String uploadFile() {
		String json = null;

		// Img uploader = new Img(request);
		// uploader.setSaveFolder(request.getParameter("folder").replace("/",
		// "\\"));
		// String[] arr = uploader.upload().split("\\\\");
		//
		// final String fileName = arr[arr.length - 1];
		//
		// json = Mapper.stringify(new Object() {
		// @SuppressWarnings("unused")
		// public Boolean isOk = true;
		// @SuppressWarnings("unused")
		// public String filename = fileName;
		// @SuppressWarnings("unused")
		// public String id = fileName;
		// });

		return json;
	}

//	public void write() {
//		if (request.hasRoute("save")) {
//			save_jsp_fileContent();
//			response.outputAction();
//		} else if (request.hasRoute("upload")) {
//			response.outputJSON(uploadFile());
//		}
//	}
}
