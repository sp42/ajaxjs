package com.ajaxjs.web.upload;

import com.ajaxjs.config.ConfigService;

/**
 * 读取配置文件的配置
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class UploadConfigByConfigFile implements UploadConfig {
	private String configNode;

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
		return ConfigService.getValueAsString(configNode + ".SaveFolder");
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
