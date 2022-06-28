/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流程定义 process 元素 ；
 */
public class ProcessModel extends BaseWfModel {
	private static final long serialVersionUID = -9000210138346445915L;

	/**
	 * 节点元素集合
	 */
	private List<NodeModel> nodes = new ArrayList<>();

	/**
	 * 
	 */
	private List<TaskModel> taskModels = new ArrayList<>();

	/**
	 * 流程实例启动 url
	 */
	private String instanceUrl;

	/**
	 * 期望完成时间
	 */
	private Date expireDate;

	/**
	 * 实例编号生成的 class
	 */
	private String instanceNoClass;

	/**
	 * lock
	 */
	private final Object lock = new Object();

	/**
	 * 返回当前流程定义的所有工作任务节点模型
	 * 
	 * @return 工作任务节点模型
	 * @deprecated
	 */
	public List<WorkModel> getWorkModels() {
		List<WorkModel> models = new ArrayList<>();

		for (NodeModel node : nodes) {
			if (node instanceof WorkModel)
				models.add((WorkModel) node);
		}

		return models;
	}

	/**
	 * 获取所有的有序任务模型集合
	 * 
	 * @return 任务模型集合
	 */
	public List<TaskModel> getTaskModels() {
		if (taskModels.isEmpty()) {
			synchronized (lock) {
				if (taskModels.isEmpty())
					buildModels(taskModels, getStart().getNextModels(TaskModel.class), TaskModel.class);
			}
		}

		return taskModels;
	}

	/**
	 * 根据指定的节点类型返回流程定义中所有模型对象
	 * 
	 * @param clazz 节点类型
	 * @param <T>   泛型
	 * @return 节点列表
	 */
	public <T> List<T> getModels(Class<T> clazz) {
		List<T> models = new ArrayList<>();
		buildModels(models, getStart().getNextModels(clazz), clazz);

		return models;
	}

	/**
	 * 
	 * @param <T>
	 * @param models
	 * @param nextModels
	 * @param clazz
	 */
	private <T> void buildModels(List<T> models, List<T> nextModels, Class<T> clazz) {
		for (T nextModel : nextModels) {
			if (!models.contains(nextModel)) {
				models.add(nextModel);
				buildModels(models, ((NodeModel) nextModel).getNextModels(clazz), clazz);
			}
		}
	}

	/**
	 * 获取 process 定义的 start 节点模型
	 * 
	 * @return start 节点模型
	 */
	public StartModel getStart() {
		for (NodeModel node : nodes) {
			if (node instanceof StartModel)
				return (StartModel) node;
		}

		return null;
	}

	/**
	 * 根据节点名称查找该节点对象并返回
	 * 
	 * @param nodeName 节点名称
	 * @return 节点对象
	 */
	public NodeModel getNode(String nodeName) {
		for (NodeModel node : nodes) {
			if (node.getName().equals(nodeName))
				return node;
		}

		return null;
	}

	/**
	 * 判断当前模型的节点是否包含给定的节点名称参数
	 * 
	 * @param <T>
	 * @param clz       特定类型节点
	 * @param nodeNames 节点名称数组
	 * @return true 表示包含给定的节点名称的节点
	 */
	public <T> boolean containsNodeNames(Class<T> clz, String... nodeNames) {
		for (NodeModel node : nodes) {
			if (!clz.isInstance(node)) // 不是这类型的，跳过
				continue;

			for (String nodeName : nodeNames) {
				if (node.getName().equals(nodeName))
					return true;
			}
		}

		return false;
	}

	public List<NodeModel> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeModel> nodes) {
		this.nodes = nodes;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

	public String getInstanceNoClass() {
		return instanceNoClass;
	}

	public void setInstanceNoClass(String instanceNoClass) {
		this.instanceNoClass = instanceNoClass;
	}

}
