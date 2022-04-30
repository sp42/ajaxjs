package com.ajaxjs.storage.file.event;

import java.util.EventObject;

import com.ajaxjs.storage.file.model.FileStatus;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class FileDeletedEvent extends EventObject {
	private static final long serialVersionUID = -5596902733635680096L;

	/**
	 * 文件状态
	 */
	private FileStatus fileStatus;

	public FileDeletedEvent(Object source, FileStatus fileStatus) {
		super(source);

		this.fileStatus = fileStatus;
	}

	public FileStatus getFileStatus() {
		return fileStatus;
	}
}
