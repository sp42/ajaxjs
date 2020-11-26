package com.ajaxjs.workflow.parser;

import com.ajaxjs.workflow.model.JoinModel;
import com.ajaxjs.workflow.model.NodeModel;

/**
 * 合并节点解析类
 
 */
public class JoinParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new JoinModel();
	}
}
