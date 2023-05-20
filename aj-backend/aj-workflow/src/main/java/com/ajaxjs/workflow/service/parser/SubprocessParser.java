package com.ajaxjs.workflow.service.parser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.node.work.SubProcessModel;

/**
 * 子流程节点解析类
 */
@Component
public class SubprocessParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new SubProcessModel();
	}

	@Value("${workflow.subProcessUrl}")
	private String subProcessUrl;

	@Override
	protected void parseNode(NodeModel node, Element element) {
		SubProcessModel model = (SubProcessModel) node;
		model.setProcessName(element.getAttribute(ATTR_PROCESS_NAME));
		String version = element.getAttribute(ATTR_VERSION);
		int ver;

		try {
			ver = Integer.parseInt(version);
		} catch (NumberFormatException e) {
			ver = 0;
		}

		model.setVersion(ver);
		String form = element.getAttribute(ATTR_FORM);
		model.setForm(StringUtils.hasText(form) ? form : subProcessUrl);
	}
}
