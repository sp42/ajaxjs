package com.ajaxjs.workflow.parser;

import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.StartModel;

/**
 * 开始节点解析类
 * 
 */
public class StartParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new StartModel();
	}
}
