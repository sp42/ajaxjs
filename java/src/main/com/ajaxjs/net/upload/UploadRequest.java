package com.ajaxjs.net.upload;

import javax.servlet.http.HttpServletRequest;

public class UploadRequest {
	/**
	 * 上传最大文件大小，默认 1 MB
	 */
	private int MaxFileSize = 1024 * 1000; 
	
	/**
	 * 保存文件的目录
	 */
	private String upload_save_folder = "E:\\temp\\";
	
	/**
	 * 上传是否成功
	 */
	private boolean isOk;
	
	/**
	 * 是否更名
	 */
	private boolean isNewName;
	
	/**
	 * 成功上传之后的文件名。如果 isNewName = false，则是原上传的名字
	 */
	private String uploaded_save_fileName;
	
	/**
	 * 相同文件名是否覆盖？true=允许覆盖
	 */
	private boolean isFileOverwrite = true;
	
	private HttpServletRequest request;

	public int getMaxFileSize() {
		return MaxFileSize;
	}


	public void setMaxFileSize(int maxFileSize) {
		MaxFileSize = maxFileSize;
	}


	public boolean isNewName() {
		return isNewName;
	}


	public void setNewName(boolean isNewName) {
		this.isNewName = isNewName;
	}


	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public String getUpload_save_folder() {
		return upload_save_folder;
	}


	public void setUpload_save_folder(String upload_save_folder) {
		this.upload_save_folder = upload_save_folder;
	}


	public String getUploaded_save_fileName() {
		return uploaded_save_fileName;
	}


	public void setUploaded_save_fileName(String uploaded_save_fileName) {
		this.uploaded_save_fileName = uploaded_save_fileName;
	}


	public boolean isOk() {
		return isOk;
	}


	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}


	public boolean isFileOverwrite() {
		return isFileOverwrite;
	}


	public void setFileOverwrite(boolean isFileOverwrite) {
		this.isFileOverwrite = isFileOverwrite;
	}
	
	
}
