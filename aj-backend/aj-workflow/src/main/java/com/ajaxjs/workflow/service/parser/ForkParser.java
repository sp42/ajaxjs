package com.ajaxjs.workflow.service.parser;

import org.springframework.stereotype.Component;

import com.ajaxjs.workflow.model.node.ForkModel;
import com.ajaxjs.workflow.model.node.NodeModel;

/**
 * 分支节点解析类
 */
@Component
public class ForkParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new ForkModel();
	}
}
