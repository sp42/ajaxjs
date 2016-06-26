package org.snaker.engine.scheduling;

import org.snaker.engine.entity.Process;
import org.snaker.engine.model.NodeModel;

import java.util.Map;

/**
 * 提醒接口
 * 
 * @author yuqs
 * @since 2.0
 */
public interface IReminder {
	/**
	 * 提醒操作
	 * 
	 * @param process
	 *            流程定义对象
	 * @param orderId
	 *            流程实例id
	 * @param taskId
	 *            任务id
	 * @param nodeModel
	 *            节点模型
	 * @param data
	 *            数据
	 */
	void remind(Process process, String orderId, String taskId, NodeModel nodeModel, Map<String, Object> data);
}
