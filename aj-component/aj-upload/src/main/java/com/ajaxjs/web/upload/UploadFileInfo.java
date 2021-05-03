/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.web.upload;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 上传的配置信息
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UploadFileInfo {
	/**
	 * 表单字段名
	 */
	public String name;

	/**
	 * 原始文件名
	 */
	public String oldFilename;

	/**
	 * 文件扩展名
	 */
	public String extName;

	/**
	 * 文件类型
	 */
	public String contentType;

	/**
	 * 文件大小
	 */
	public int contentLength;

	/**
	 * 单次文件上传最大字节
	 */
	public int maxTotalFileSize = 1024 * 5000;

	/**
	 * 单个文件上传最大字节
	 */
	public int maxSingleFileSize = 1024 * 1000; // 默认 1 MB;

	/**
	 * 允许上传的文件类型，如果为空数组则不限制上传类型。格式如 {".jpg", ".png", ...}
	 */
//	public String[] allowExtFilenames = new String[] { "jpeg", "jpg", "png", "gif" };
	public String[] allowExtFilenames;

	/**
	 * 相同文件名是否覆盖？true=允许覆盖
	 */
	public boolean isFileOverwrite;

	/**
	 * 保存文件的目录
	 */
	public String saveFolder = "c:\\temp\\";

	/**
	 * 是否按照表单里的名字，还是改名？请在外部决定
	 * 
	 */
	public String saveFileName;

	/**
	 * 上传成功之文件之目录+文件名（从 web 根目录开始起）
	 */
	public String path;

	/**
	 * 上传成功之文件完整的磁盘路径
	 */
	public String fullPath;

	/**
	 * 外界访问的 web url
	 */
	public String visitPath;

	/**
	 * 是否上传成功
	 */
	public boolean isOk;

	/**
	 * 若不成功，是什么异常信息
	 */
	public String errMsg;

	/**
	 * 上传之前触发的事件
	 */
	public Function<UploadFileInfo, Boolean> beforeUpload;
	
	/**
	 * 上传之后触发的事件
	 */
	public Consumer<UploadFileInfo> afterUpload;
}
