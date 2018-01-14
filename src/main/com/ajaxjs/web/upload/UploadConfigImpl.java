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
 * 默认配置实现类
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class UploadConfigImpl implements UploadConfig {
	@Override
	public int getMaxTotalFileSize() {
		return 1024 * 5000;
	}

	@Override
	public int getMaxSingleFileSize() {
		return 1024 * 1000; // 默认 1 MB
	}

	static String[] ext = new String[] { ".jpg", ".png", ".gif" };

	@Override
	public String[] getAllowExtFilenames() {
		return ext;
	}

	@Override
	public boolean isFileOverwrite() {
		return true;
	}

	@Override
	public String getSaveFolder() {
		return "c:\\temp\\";
	}

	@Override
	public String getFileName(MetaData meta) {
		//		return "" + System.currentTimeMillis();
		return meta.filename;
	}
}
