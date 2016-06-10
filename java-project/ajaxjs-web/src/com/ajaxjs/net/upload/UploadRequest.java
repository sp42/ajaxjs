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
package com.ajaxjs.net.upload;

import javax.servlet.http.HttpServletRequest;

/**
 * 上传请求的 bean，包含所有有关请求的信息
 * @author frank
 *
 */
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

	/**
	 * @return the maxFileSize
	 */
	public int getMaxFileSize() {
		return MaxFileSize;
	}

	/**
	 * @param maxFileSize the maxFileSize to set
	 */
	public void setMaxFileSize(int maxFileSize) {
		MaxFileSize = maxFileSize;
	}

	/**
	 * @return the upload_save_folder
	 */
	public String getUpload_save_folder() {
		return upload_save_folder;
	}

	/**
	 * @param upload_save_folder the upload_save_folder to set
	 */
	public void setUpload_save_folder(String upload_save_folder) {
		this.upload_save_folder = upload_save_folder;
	}

	/**
	 * @return the isOk
	 */
	public boolean isOk() {
		return isOk;
	}

	/**
	 * @param isOk the isOk to set
	 */
	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	/**
	 * @return the isNewName
	 */
	public boolean isNewName() {
		return isNewName;
	}

	/**
	 * @param isNewName the isNewName to set
	 */
	public void setNewName(boolean isNewName) {
		this.isNewName = isNewName;
	}

	/**
	 * @return the uploaded_save_fileName
	 */
	public String getUploaded_save_fileName() {
		return uploaded_save_fileName;
	}

	/**
	 * @param uploaded_save_fileName the uploaded_save_fileName to set
	 */
	public void setUploaded_save_fileName(String uploaded_save_fileName) {
		this.uploaded_save_fileName = uploaded_save_fileName;
	}

	/**
	 * @return the isFileOverwrite
	 */
	public boolean isFileOverwrite() {
		return isFileOverwrite;
	}

	/**
	 * @param isFileOverwrite the isFileOverwrite to set
	 */
	public void setFileOverwrite(boolean isFileOverwrite) {
		this.isFileOverwrite = isFileOverwrite;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	
}
