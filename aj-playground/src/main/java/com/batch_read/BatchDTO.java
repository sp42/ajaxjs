package com.batch_read;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class BatchDTO implements Serializable {
	private static final long serialVersionUID = -380954097612127601L;

	private String pkBtTaskId = "";
	private String taskName = "";
	private String actionType = "";
	private String taskDesc = "";
	private String status = "";
	private String commitedBy = "";
	private Date commitedTime = null;
	private String resultLog = "";
	private String batchId = "";
	private boolean headSkip = true;
	private String errorLogPath = "";
	private String logRootPath = "";
	private boolean errorFlag = false;
	private String campId = "";
	private String[] data = null;
	private long totalCount = 0;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionType == null) ? 0 : actionType.hashCode());
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result + ((campId == null) ? 0 : campId.hashCode());
		result = prime * result + ((commitedBy == null) ? 0 : commitedBy.hashCode());
		result = prime * result + ((commitedTime == null) ? 0 : commitedTime.hashCode());
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + (errorFlag ? 1231 : 1237);
		result = prime * result + ((errorLogPath == null) ? 0 : errorLogPath.hashCode());
		result = prime * result + (headSkip ? 1231 : 1237);
		result = prime * result + ((logRootPath == null) ? 0 : logRootPath.hashCode());
		result = prime * result + ((pkBtTaskId == null) ? 0 : pkBtTaskId.hashCode());
		result = prime * result + ((resultLog == null) ? 0 : resultLog.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((taskDesc == null) ? 0 : taskDesc.hashCode());
		result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
		result = prime * result + (int) (totalCount ^ (totalCount >>> 32));
		return result;
	}

	public String getPkBtTaskId() {
		return pkBtTaskId;
	}

	public void setPkBtTaskId(String pkBtTaskId) {
		this.pkBtTaskId = pkBtTaskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCommitedBy() {
		return commitedBy;
	}

	public void setCommitedBy(String commitedBy) {
		this.commitedBy = commitedBy;
	}

	public Date getCommitedTime() {
		return commitedTime;
	}

	public void setCommitedTime(Date commitedTime) {
		this.commitedTime = commitedTime;
	}

	public String getResultLog() {
		return resultLog;
	}

	public void setResultLog(String resultLog) {
		this.resultLog = resultLog;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public boolean isHeadSkip() {
		return headSkip;
	}

	public void setHeadSkip(boolean headSkip) {
		this.headSkip = headSkip;
	}

	public String getErrorLogPath() {
		return errorLogPath;
	}

	public void setErrorLogPath(String errorLogPath) {
		this.errorLogPath = errorLogPath;
	}

	public String getLogRootPath() {
		return logRootPath;
	}

	public void setLogRootPath(String logRootPath) {
		this.logRootPath = logRootPath;
	}

	public boolean isErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getCampId() {
		return campId;
	}

	public void setCampId(String campId) {
		this.campId = campId;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BatchDTO)) {
			return false;
		}
		BatchDTO other = (BatchDTO) obj;
		if (actionType == null) {
			if (other.actionType != null) {
				return false;
			}
		} else if (!actionType.equals(other.actionType)) {
			return false;
		}
		if (batchId == null) {
			if (other.batchId != null) {
				return false;
			}
		} else if (!batchId.equals(other.batchId)) {
			return false;
		}
		if (campId == null) {
			if (other.campId != null) {
				return false;
			}
		} else if (!campId.equals(other.campId)) {
			return false;
		}
		if (commitedBy == null) {
			if (other.commitedBy != null) {
				return false;
			}
		} else if (!commitedBy.equals(other.commitedBy)) {
			return false;
		}
		if (commitedTime == null) {
			if (other.commitedTime != null) {
				return false;
			}
		} else if (!commitedTime.equals(other.commitedTime)) {
			return false;
		}
		if (!Arrays.equals(data, other.data)) {
			return false;
		}
		if (errorFlag != other.errorFlag) {
			return false;
		}
		if (errorLogPath == null) {
			if (other.errorLogPath != null) {
				return false;
			}
		} else if (!errorLogPath.equals(other.errorLogPath)) {
			return false;
		}
		if (headSkip != other.headSkip) {
			return false;
		}
		if (logRootPath == null) {
			if (other.logRootPath != null) {
				return false;
			}
		} else if (!logRootPath.equals(other.logRootPath)) {
			return false;
		}
		if (pkBtTaskId == null) {
			if (other.pkBtTaskId != null) {
				return false;
			}
		} else if (!pkBtTaskId.equals(other.pkBtTaskId)) {
			return false;
		}
		if (resultLog == null) {
			if (other.resultLog != null) {
				return false;
			}
		} else if (!resultLog.equals(other.resultLog)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
			return false;
		}
		if (taskDesc == null) {
			if (other.taskDesc != null) {
				return false;
			}
		} else if (!taskDesc.equals(other.taskDesc)) {
			return false;
		}
		if (taskName == null) {
			if (other.taskName != null) {
				return false;
			}
		} else if (!taskName.equals(other.taskName)) {
			return false;
		}
		if (totalCount != other.totalCount) {
			return false;
		}
		return true;
	}

}