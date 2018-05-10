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

/**
 * 读取配置文件的配置
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class UploadConfigByConfigFile implements UploadConfig {
	private String configNode;
	// 适合 web 的绝对路径
	private String absolutePath;

	/**
	 * 检查是否有对应的配置 默认是 "uploadFile"
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

	@Override
	public String getSaveFolder() {
		return ConfigService.getValueAsBool(configNode + ".isUsingRelativePath") ? 
				
				getAbsolutePath() + File.separator: ConfigService.getValueAsString(configNode + ".SaveFolder");
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

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

}
