package com.ajaxjs.storage.file.event;

import java.util.EventObject;

import com.ajaxjs.storage.file.model.FileStatus;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class FileUploadedEvent extends EventObject {
	private static final long serialVersionUID = -1446262821520033689L;

	private FileStatus fileStatus;

	public FileUploadedEvent(Object source, FileStatus fileStatus) {
		super(source);

		this.fileStatus = fileStatus;
	}

	public FileStatus getFileStatus() {
		return fileStatus;
	}
}
