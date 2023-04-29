package com.ajaxjs.storage.index;

import java.util.Date;

/**
 * @TableName("ufs_index")
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class IndexStatus {
	public static final int STATE_PENDING = 0;
	public static final int STATE_SUCCESS = 1;
	public static final int STATE_FAILURE = -1;

	public static final Integer OCR_STATE_PENDING = 0;
	public static final Integer OCR_STATE_SUCCESS = 1;
	public static final Integer OCR_STATE_FAILURE = -1;

	public static final Integer TIKA_STATE_PENDING = 0;
	public static final Integer TIKA_STATE_SUCCESS = 1;
	public static final Integer TIKA_STATE_FAILURE = -1;

	public static final Integer UPDATE_STATE_PENDING = 0;
	public static final Integer UPDATE__STATE_SUCCESS = 1;
	public static final Integer UPDATE__STATE_FAILURE = -1;

	private Long id;

	private Long fileId;

	private int state = STATE_PENDING;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Integer getOcrState() {
		return ocrState;
	}

	public void setOcrState(Integer ocrState) {
		this.ocrState = ocrState;
	}

	public Integer getUpdateState() {
		return updateState;
	}

	public void setUpdateState(Integer updateState) {
		this.updateState = updateState;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public Integer getTakeTime() {
		return takeTime;
	}

	public void setTakeTime(Integer takeTime) {
		this.takeTime = takeTime;
	}

	public Long getIndexServerId() {
		return indexServerId;
	}

	public void setIndexServerId(Long indexServerId) {
		this.indexServerId = indexServerId;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Date lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	private Integer ocrState;

	private Integer updateState;

	private int retries = 0;

	private Integer takeTime;

	private Long indexServerId;

	private Date startedAt;

	private Date createdAt;

	private Date lastUpdatedAt;
}
