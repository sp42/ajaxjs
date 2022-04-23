/*
 * Copyright 2013-2015 www.snakerflow.com. * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in
 * compliance with the License. * You may obtain a copy of the License at * * http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by
 * applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the
 * License.
 */
package com.ajaxjs.workflow.scheduling;

import java.util.Map;

import com.ajaxjs.workflow.model.NodeModel;

/**
 * 提醒接口
 * 
 */
public interface IReminder {
	/**
	 * 提醒操作
	 * 
	 * @param process   流程定义对象
	 * @param orderId   流程实例id
	 * @param taskId    任务id
	 * @param nodeModel 节点模型
	 * @param data      数据
	 */
	void remind(Process process, Long orderId, Long taskId, NodeModel nodeModel, Map<String, Object> data);
}
