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
package com.ajaxjs.workflow.service.parser;

import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import com.ajaxjs.workflow.model.node.DecisionModel;
import com.ajaxjs.workflow.model.node.NodeModel;

/**
 * 决策节点解析类
 */
@Component
public class DecisionParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new DecisionModel();
	}

	@Override
	protected void parseNode(NodeModel node, Element element) {
		DecisionModel decision = (DecisionModel) node; // 解析 decisition 节点的特有属性 expr
		System.out.println("::::::::" + element);
		System.out.println("::::::::" + element.getAttribute(ATTR_EXPR));

		String v = element.getAttribute(ATTR_HANDLECLASS);
		System.out.println(v);
		decision.setExpr(element.getAttribute(ATTR_EXPR));
		decision.setHandleClass(element.getAttribute(ATTR_HANDLECLASS));
	}
}
