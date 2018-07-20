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

import java.io.File;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.mvc.controller.MvcRequest;

/**
 * 读取配置文件的配置
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class UploadConfigByConfigFile implements UploadConfig {
	private String configNode;

	/**
	 * 检查是否有对应的配置 默认是 "uploadFile"，可以指定该项来提供不同的上传规则
	 */
	public UploadConfigByConfigFile() {
		this("uploadFile");

	}

	/**
	 * 
	 * @param configNode
	 *            配置节点
	 */
	public UploadConfigByConfigFile(String configNode) {
		if (ConfigService.config.get(configNode) == null)
			throw new NullPointerException("配置文件中缺少对应的上传配置内容：" + configNode);
		else
			this.configNode = configNode;
	}

	@Override
	public int getMaxTotalFileSize() {
		return ConfigService.getValueAsInt(configNode + ".MaxTotalFileSize");
	}

	@Override
	public int getMaxSingleFileSize() {
		return ConfigService.getValueAsInt(configNode + ".MaxSingleFileSize");
	}

	@Override
	public String[] getAllowExtFilenames() {
		return null;
	}

	@Override
	public boolean isFileOverwrite() {
		return ConfigService.getValueAsBool(configNode + ".isFileOverwrite");
	}

	private String saveFolder;

	@Override
	public String getSaveFolder() {
		return saveFolder;
	}

	public void setSaveFolder(String saveFolder) {
		this.saveFolder = saveFolder;
	}

	/**
	 * 
	 * @param request
	 */
	public void setSaveFolder(MvcRequest request) {
		String relativePath = ConfigService.getValueAsString(configNode + ".saveFolder.relativePath");

		if (ConfigService.getValueAsBool(configNode + ".saveFolder.isUsingRelativePath")) {
			setSaveFolder(request.mappath(relativePath) + File.separator );
		} else {
			setSaveFolder(ConfigService.getValueAsString(configNode + ".saveFolder.absolutePath"));
		}
	}

	@Override
	public String getFileName(MetaData meta) {
		return meta.filename;
	}

	public String getConfigNode() {
		return configNode;
	}

	public void setConfigNode(String configNode) {
		this.configNode = configNode;
	}

}
