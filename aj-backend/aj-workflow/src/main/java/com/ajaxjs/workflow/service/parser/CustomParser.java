package com.ajaxjs.workflow.service.parser;

import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.node.work.CustomModel;

/**
 * 自定义节点解析器
 */
@Component
public class CustomParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new CustomModel();
	}

	@Override
	protected void parseNode(NodeModel node, Element element) {
		CustomModel custom = (CustomModel) node;
		custom.setClazz(element.getAttribute(ATTR_CLAZZ));
		custom.setMethodName(element.getAttribute(ATTR_METHODNAME));
		custom.setArgs(element.getAttribute(ATTR_ARGS));
		custom.setVar(element.getAttribute(ATTR_VAR));
	}
}
