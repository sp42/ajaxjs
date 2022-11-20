package com.ajaxjs.workflow.model.po;

import java.util.Date;
import java.util.Map;

import com.ajaxjs.sql.annotation.IgnoreDB;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.model.TaskModel;

/**
 * 任务实体类
 */
public class TaskPO extends WfPersistantObject {
	/**
	 * 
	 */
	public static final String KEY_ACTOR = "S-ACTOR";

	/**
	 * 版本
	 */
	private Integer version;

	/**
	 * 流程实例ID
	 */
	private Long orderId;

	/**
	 * 任务显示名称
	 */
	private String displayName;

	/**
	 * 参与方式（0：普通任务；1：参与者会签任务）
	 */
	private Integer performType;

	/**
	 * 任务类型（0：主办任务；1：协办任务）
	 */
	private TaskType taskType;

	/**
	 * 任务处理者ID
	 */
	private Long operator;

	/**
	 * 任务完成时间
	 */
	private Date finishDate;

	/**
	 * 期望的完成时间date类型
	 */
	private Date expireDate;

	/**
	 * 提醒时间date类型
	 */
	private Date remindDate;

	/**
	 * 任务关联的表单url
	 */
	private String actionUrl;
	/**
	 * 任务参与者列表
	 */
	private Long[] actorIds;

	/**
	 * 父任务Id
	 */
	private Long parentId;

	/**
	 * 任务附属变量
	 */
	private String variable;

	/**
	 * 保持模型对象
	 */
	private TaskModel model;

	public TaskPO() {
	}

	public TaskPO(Long id) {
		setId(id);
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	@IgnoreDB
	public Long[] getActorIds() {
		if (actorIds == null) {
			Map<String, Object> map = JsonHelper.parseMap(variable);

			if (map != null && map.get(KEY_ACTOR) != null) {
				String actorStr = (String) map.get(KEY_ACTOR);
				actorIds = WfUtils.cast(actorStr.split(","));
			}
		}

		return actorIds;
	}

	public void setActorIds(Long[] actorIds) {
		this.actorIds = actorIds;
	}

	public Integer getPerformType() {
		return performType;
	}

	public void setPerformType(Integer performType) {
		this.performType = performType;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Date getRemindDate() {
		return remindDate;
	}

	public void setRemindDate(Date remindDate) {
		this.remindDate = remindDate;
	}

	@IgnoreDB
	public TaskModel getModel() {
		return model;
	}

	public void setModel(TaskModel model) {
		this.model = model;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Task(id=").append(getId());
		sb.append(",orderId=").append(orderId);
		sb.append(",taskName=").append(getName());
		sb.append(",displayName").append(displayName);
		sb.append(",taskType=").append(taskType);
		sb.append(",createDate=").append(getCreateDate());
		sb.append(",performType=").append(performType).append(")");

		return sb.toString();
	}

	/**
	 * 任务是否主办类型
	 * 
	 * @return true 表示为主办类型
	 */
	@IgnoreDB
	public boolean isMajor() {
		return getTaskType() == TaskType.MAJOR;
	}
}
