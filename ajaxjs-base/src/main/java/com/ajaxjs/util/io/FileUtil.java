/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.util.io;

import java.io.File;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 文件工具类。 可返回本实例供链式调用
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class FileUtil extends StreamChain<FileUtil> {
	static final LogHelper LOGGER = LogHelper.getLog(FileUtil.class);

	private String filePath;

	private File file;

	private boolean overwrite;
	


	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public FileUtil setFilePath(String filePath) {
		this.filePath = filePath;
		file = new File(filePath); // 同时设置 File 对象

		return this;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public FileUtil setFile(File file) {
		this.file = file;
		return this;
	}

	/**
	 * @return the overwrite
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * @param overwrite the overwrite to set
	 */
	public FileUtil setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
		return this;
	}
}
