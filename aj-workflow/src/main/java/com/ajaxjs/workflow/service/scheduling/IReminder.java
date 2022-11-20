package com.ajaxjs.workflow.service.scheduling;

import java.util.Map;

import com.ajaxjs.workflow.model.node.NodeModel;

/**
 * 提醒接口
 * 
 */
public interface IReminder {
	/**
	 * 提醒操作
	 * 
	 * @param process   流程定义对象
	 * @param orderId   流程实例 id
	 * @param taskId    任务 id
	 * @param nodeModel 节点模型
	 * @param data      数据
	 */
	void remind(Process process, Long orderId, Long taskId, NodeModel nodeModel, Map<String, Object> data);
}
