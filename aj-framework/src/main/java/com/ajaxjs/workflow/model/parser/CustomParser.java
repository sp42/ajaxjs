/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.workflow.model.parser;

import org.w3c.dom.Element;

import com.ajaxjs.workflow.model.CustomModel;
import com.ajaxjs.workflow.model.NodeModel;

/**
 * 自定义节点解析器
 * 
 */
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
