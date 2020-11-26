package com.ajaxjs.workflow.parser;

import org.w3c.dom.Element;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.SubProcessModel;

/**
 * 子流程节点解析类
 * 
 */
public class SubprocessParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new SubProcessModel();
	}

	@Override
	protected void parseNode(NodeModel node, Element element) {
		SubProcessModel model = (SubProcessModel) node;
		model.setProcessName(element.getAttribute(ATTR_PROCESSNAME));
		String version = element.getAttribute(ATTR_VERSION);

		int ver;
		try {
			ver = Integer.parseInt(version);
		} catch (NumberFormatException e) {
			ver = 0;
		}

		model.setVersion(ver);
		String form = element.getAttribute(ATTR_FORM);
		model.setForm(CommonUtil.isEmptyString(form) ? ConfigService.getValueAsString("workflow.subprocessurl") : form);
	}
}
