package com.ajaxjs.workflow.parser;

import org.w3c.dom.Element;

import com.ajaxjs.workflow.model.DecisionModel;
import com.ajaxjs.workflow.model.NodeModel;

/**
 * 决策节点解析类
 * 
 */
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
