package com.ajaxjs.storage.file.request;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ZipRequest {
	/**
	 * 压缩条目
	 */
	private List<ZipEntry> entries;

	/**
	 * 压缩文件备注
	 */
	private String comment;

	/**
	 * 压缩级别 0-9
	 */
	private Integer level;

	private String filename;

	/**
	 * 要进行签要的 HTTP 请求头
	 */
	private Map<String, String> requestHeaders;

	public static class ZipEntry {
		/**
		 * 文件 ID，如果为空则表示该条目是一个文件夹
		 */
		private String fileId;

		/**
		 * 文件夹/文件合路径，例如：a/b/c
		 */
		private String fullPath;

		// @ApiModelProperty(hidden = true)
		public Long getFileIdAsLong() {
			return fileId == null ? null : Long.valueOf(fileId);
		}

		public String getFileId() {
			return fileId;
		}

		public void setFileId(String fileId) {
			this.fileId = fileId;
		}

		public String getFullPath() {
			return fullPath;
		}

		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}
	}

	public List<ZipEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ZipEntry> entries) {
		this.entries = entries;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Map<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

}
