package com.ajaxjs.workflow.parser;

import com.ajaxjs.workflow.model.ForkModel;
import com.ajaxjs.workflow.model.NodeModel;

/**
 * 分支节点解析类
 * 
 */
public class ForkParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new ForkModel();
	}
}
