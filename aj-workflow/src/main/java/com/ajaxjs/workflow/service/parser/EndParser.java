package com.ajaxjs.workflow.service.parser;

import org.springframework.stereotype.Component;

import com.ajaxjs.workflow.model.node.EndModel;
import com.ajaxjs.workflow.model.node.NodeModel;

/**
 * 结束节点解析类
 */
@Component
public class EndParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new EndModel();
	}
}
