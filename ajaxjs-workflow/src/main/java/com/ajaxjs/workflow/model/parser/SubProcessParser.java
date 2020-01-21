/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model.parser;

import org.w3c.dom.Element;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.SubProcessModel;

/**
 * 子流程节点解析类
 * 
 * @author yuqs
 * @since 1.0
 */
public class SubProcessParser extends AbstractNodeParser {
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

	@Override
	protected NodeModel newModel() {
		return new SubProcessModel();
	}
}
