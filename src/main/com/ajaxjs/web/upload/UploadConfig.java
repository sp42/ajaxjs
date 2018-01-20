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

/**
 * 配置接口
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
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
	 * @return 允许上传的文件类型
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
	 * @return 保存文件的目录
	 */
	public String getSaveFolder();

	/**
	 * 是否按照表单里的名字，还是改名？在这里决定
	 * 
	 * @param meta
	 *            元数据
	 * @return 文件名
	 */
	public String getFileName(MetaData meta);
}
