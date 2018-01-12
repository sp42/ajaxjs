package com.ajaxjs.web.upload;

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
