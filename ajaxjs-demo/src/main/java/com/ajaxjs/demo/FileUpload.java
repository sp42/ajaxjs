package com.ajaxjs.demo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.web.UploadFile;
import com.ajaxjs.web.UploadFileInfo;
import com.ajaxjs.web.mvc.controller.IController;

/**
 * 接收文件上传
 */
@Path("/FileUpload")
public class FileUpload implements IController {

	@POST
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.maxSingleFileSize = 1024 * 50000; // 指定文件上传大小上限 50 MB;
		uploadFileInfo.allowExtFilenames = new String[] { "txt", "jpg" }; // 文件类型
		uploadFileInfo.isFileOverwrite = true; // 相同文件名是否直接覆盖
		uploadFileInfo.saveFolder = "c:\\temp\\"; // 保存目录
		
		UploadFile uploadRequest = new UploadFile(request, uploadFileInfo);
		
		try {
			
			uploadRequest.upload();
			response.getWriter().append(uploadFileInfo.isOk ? "Upload OK!" : "Upload failed!");
		} catch (Throwable e) {
			response.getWriter().append(e.toString());
		}
	}
}
