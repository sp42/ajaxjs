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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.logger.LogHelper;
 
/**
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class PageEditorService {
	private static final LogHelper LOGGER = LogHelper.getLog(PageEditorService.class);
	
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
	 *  获取指定目录内的图片 目录有 folder 参数指定
	 * @param folder 
	 * @return
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
				if (strFileName.contains(".jpg")
						|| strFileName.contains(".gif")
						|| strFileName.contains(".png")) {

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
	 * 
	 * @param file rh.Mappath(rh.get("file")
	 * @return
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
	

	public void write() {
//
//		if (request.hasRoute("save")) {
//			save_jsp_fileContent();
//			response.outputAction();
//		} else if (request.hasRoute("upload")) {
//			response.outputJSON(uploadFile());
//		}
	}
}
