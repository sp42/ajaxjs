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
package com.ajaxjs.workflow.service.parser;

import org.springframework.stereotype.Component;

import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.node.StartModel;

/**
 * 开始节点解析类
 */
@Component
public class StartParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new StartModel();
	}
}
