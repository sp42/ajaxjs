package com.ajaxjs.workflow.parser;

import com.ajaxjs.workflow.model.EndModel;
import com.ajaxjs.workflow.model.NodeModel;

/**
 * 结束节点解析类
 * 
 */
public class EndParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new EndModel();
	}
}
