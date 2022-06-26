package com.ajaxjs.file_upload;

/**
 * 云空间的文件上传
 * 
 * @author Frank Cheung
 *
 */
public interface IFileUpload {
	/**
	 * 上传文件
	 * 
	 * @param filename
	 * @param bytes
	 * @return
	 */
	public boolean upload(String filename, byte[] bytes);
}
