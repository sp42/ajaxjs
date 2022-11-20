/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.service.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.model.node.NodeModel;

/**
 * 抽象 DOM 节点解析类 完成通用的属性、变迁解析
 * 
 */
public abstract class AbstractNodeParser {
	/**
	 * 模型对象，解析成功后，提供返回 NodeModel 模型对象
	 */
	protected NodeModel model;

	/**
	 * 抽象方法，由子类产生各自的模型对象
	 * 
	 * @return 模型对象
	 */
	protected abstract NodeModel newModel();

	/**
	 * 子类可覆盖此方法，完成特定的解析。相当于后续执行
	 * 
	 * @param model   节点模型
	 * @param element DOM 元素
	 */
	protected void parseNode(NodeModel model, Element element) {
	}

	/**
	 * 节点 DOM 元素解析方法，由实现类完成解析。 由子类产生各自的模型对象，设置常用的名称属性，并且解析子节点 transition，构造
	 * TransitionModel 模型对象
	 * 
	 * @param element DOM 元素
	 */
	public void parse(Element element) {
		model = newModel();
		model.setName(element.getAttribute(ATTR_NAME));
		model.setDisplayName(element.getAttribute(ATTR_DISPLAYNAME));
		model.setLayout(element.getAttribute(ATTR_LAYOUT));
		model.setPreInterceptors(element.getAttribute(ATTR_PREINTERCEPTORS));
		model.setPostInterceptors(element.getAttribute(ATTR_POSTINTERCEPTORS));

		List<Element> transitions = elements(element, NODE_TRANSITION);

		for (Element te : transitions) {
			TransitionModel transition = new TransitionModel();

			transition.setName(te.getAttribute(ATTR_NAME));
			transition.setDisplayName(te.getAttribute(ATTR_DISPLAYNAME));
			transition.setTo(te.getAttribute(ATTR_TO));
			transition.setExpr(te.getAttribute(ATTR_EXPR));
			transition.setG(te.getAttribute(ATTR_G));
			transition.setOffset(te.getAttribute(ATTR_OFFSET));
			transition.setSource(model);

			model.getOutputs().add(transition);
		}

		parseNode(model, element);
	}

	/**
	 * 从element元素查找所有tagName指定的子节点元素集合
	 * 
	 * @param element XML 父元素
	 * @param tagName 子元素标签名称
	 * @return 子节点元素集合
	 */
	private static List<Element> elements(Element element, String tagName) {
		if (element == null || !element.hasChildNodes())
			return Collections.emptyList();

		List<Element> elements = new ArrayList<>();

		for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) child;

				if (tagName.equals(childElement.getNodeName()))
					elements.add(childElement);
			}
		}

		return elements;
	}

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
}
