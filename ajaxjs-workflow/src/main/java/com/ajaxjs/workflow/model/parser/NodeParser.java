/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.workflow.model.parser;

import org.snaker.engine.core.ServiceContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ajaxjs.util.XMLHelper;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.TransitionModel;

/**
 * 节点解析接口
 * 
 */
public interface NodeParser {
	/**
	 * 变迁节点名称
	 */
	public static final String NODE_TRANSITION = "transition";

	/**
	 * 节点属性名称
	 */
	public static final String ATTR_NAME = "name";
	public static final String ATTR_DISPLAYNAME = "displayName";
	public static final String ATTR_INSTANCEURL = "instanceUrl";
	public static final String ATTR_INSTANCENOCLASS = "instanceNoClass";
	public static final String ATTR_EXPR = "expr";
	public static final String ATTR_HANDLECLASS = "handleClass";
	public static final String ATTR_FORM = "form";
	public static final String ATTR_FIELD = "field";
	public static final String ATTR_VALUE = "value";
	public static final String ATTR_ATTR = "attr";
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_ASSIGNEE = "assignee";
	public static final String ATTR_ASSIGNEE_HANDLER = "assignmentHandler";
	public static final String ATTR_PERFORMTYPE = "performType";
	public static final String ATTR_TASKTYPE = "taskType";
	public static final String ATTR_TO = "to";
	public static final String ATTR_PROCESSNAME = "processName";
	public static final String ATTR_VERSION = "version";
	public static final String ATTR_EXPIRETIME = "expireTime";
	public static final String ATTR_AUTOEXECUTE = "autoExecute";
	public static final String ATTR_CALLBACK = "callback";
	public static final String ATTR_REMINDERTIME = "reminderTime";
	public static final String ATTR_REMINDERREPEAT = "reminderRepeat";
	public static final String ATTR_CLAZZ = "clazz";
	public static final String ATTR_METHODNAME = "methodName";
	public static final String ATTR_ARGS = "args";
	public static final String ATTR_VAR = "var";
	public static final String ATTR_LAYOUT = "layout";
	public static final String ATTR_G = "g";
	public static final String ATTR_OFFSET = "offset";
	public static final String ATTR_PREINTERCEPTORS = "preInterceptors";
	public static final String ATTR_POSTINTERCEPTORS = "postInterceptors";

	/**
	 * 节点dom元素解析方法，由实现类完成解析
	 * 
	 * @param element DOM 元素
	 */
	public void parse(Element element);

	/**
	 * 解析成功后，提供返回 NodeModel 模型对象
	 * 
	 * @return 节点模型
	 */
	public NodeModel getModel();

	/**
	 * 解析流程定义文件，并将解析后的对象放入模型容器中
	 * 
	 * @param processXml
	 */
	public static ProcessModel parse(String processXml) {
		ProcessModel process = new ProcessModel();

		XMLHelper.parseXML(processXml, (el, nodeList) -> {
			process.setName(el.getAttribute(ATTR_NAME));
			process.setDisplayName(el.getAttribute(ATTR_DISPLAYNAME));
			process.setExpireDate(el.getAttribute(ATTR_EXPIRETIME));
			process.setInstanceUrl(el.getAttribute(ATTR_INSTANCEURL));
			process.setInstanceNoClass(el.getAttribute(ATTR_INSTANCENOCLASS));

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
						// TODO
//						NodeModel model = parseModel(node);
//						process.getNodes().add(model);
				}
			}

			// 循环节点模型，构造变迁输入、输出的source、target
			for (NodeModel node : process.getNodes()) {
				for (TransitionModel transition : node.getOutputs()) {
					String to = transition.getTo();

					for (NodeModel node2 : process.getNodes()) {
						if (to.equalsIgnoreCase(node2.getName())) {
							node2.getInputs().add(transition);
							transition.setTarget(node2);
						}
					}
				}
			}
		});

		return process;
	}

	/**
	 * 对流程定义xml的节点，根据其节点对应的解析器解析节点内容
	 * 
	 * @param node DOM 节点
	 * @return
	 */
	static NodeModel parseModel(Node node) {
		NodeParser nodeParser = ServiceContext.getContext().findByName(node.getNodeName(), NodeParser.class);
		nodeParser.parse((Element) node);

		return nodeParser.getModel();

	}
}
