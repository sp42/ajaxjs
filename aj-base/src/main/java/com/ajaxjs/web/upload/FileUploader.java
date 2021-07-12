package com.ajaxjs.web.upload;

import javax.servlet.http.HttpServletRequest;

public interface FileUploader {
	/**
	 * 上传文件
	 * 
	 * @param req
	 * @param info
	 */
	public void uploadFile(HttpServletRequest req, UploadFileInfo info);
}
