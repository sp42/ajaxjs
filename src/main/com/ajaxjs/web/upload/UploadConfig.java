package com.ajaxjs.web.upload;

public interface UploadConfig {
	/**
	 * 单次文件上传最大字节
	 * 
	 * @return 单次文件上传最大字节
	 */
	public int getMaxTotalFileSize();

	/**
	 * 单个文件上传最大字节
	 * 
	 * @return 单个文件上传最大字节
	 */
	public int getMaxSingleFileSize();

	/**
	 * 允许上传的文件类型，如果为空数组则不限制上传类型。格式如 {".jpg", ".png", ...}
	 * 
	 * @return
	 */
	public String[] getAllowExtFilenames();

	/**
	 * 相同文件名是否覆盖？
	 * 
	 * @return true=允许覆盖
	 */
	public boolean isFileOverwrite();

	/**
	 * 保存文件的目录
	 * 
	 * @return
	 */
	public String getSaveFolder();

	/**
	 * 是否按照表单里的名字，还是改名？在这里决定
	 * @param meta
	 * @return
	 */
	public String getFileName(MetaData meta);
}
