package com.ajaxjs.web;

public class UploadFileInfo {
	/**
	 * 表单字段名
	 */
	public String name;

	/**
	 * 文件名
	 */
	public String filename;

	/**
	 * 文件类型
	 */
	public String contentType;

	/**
	 * 文件大小
	 */
	public int contentLength;

	/////////////// 以下是上传之后的信息

	/**
	 * 单次文件上传最大字节
	 */
	public int maxTotalFileSize;

	/**
	 * 单个文件上传最大字节
	 */
	public int maxSingleFileSize;

	/**
	 * 允许上传的文件类型，如果为空数组则不限制上传类型。格式如 {".jpg", ".png", ...}
	 */
	public String[] allowExtFilenames;

	/**
	 * 相同文件名是否覆盖？true=允许覆盖
	 */
	public boolean isFileOverwrite;

	/**
	 * 保存文件的目录
	 */
	public String saveFolder;

	/**
	 * 是否按照表单里的名字，还是改名？在这里决定
	 * 
	 */
	public String saveFileName;
}
