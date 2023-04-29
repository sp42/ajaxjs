package com.ajaxjs.storage.file.event;

import java.util.EventObject;
import java.util.Map;

import com.ajaxjs.storage.file.model.FileStatus;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class FileUpdatedEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private FileStatus fileStatus;

	/**
	 * 
	 */
	private Map<String, String> partialMetadata;

	public FileUpdatedEvent(Object source, FileStatus fileStatus, Map<String, String> metadata) {
		super(source);

		this.fileStatus = fileStatus;
		this.partialMetadata = metadata;
	}

	public FileStatus getFileStatus() {
		return fileStatus;
	}

	public Map<String, String> getPartialMetadata() {
		return partialMetadata;
	}
}
