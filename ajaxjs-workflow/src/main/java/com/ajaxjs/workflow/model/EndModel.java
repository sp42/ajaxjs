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
package com.ajaxjs.workflow.model;

import java.util.Collections;
import java.util.List;

import com.ajaxjs.workflow.handler.EndProcessHandler;

/**
 * 结束节点end元素
 * 
 * @author yuqs
 * @since 1.0
 */
public class EndModel extends NodeModel {
	private static final long serialVersionUID = -7793175180140842894L;

	@Override
	public void exec(Execution execution) {
		fire(new EndProcessHandler(), execution);
	}

	/**
	 * 结束节点无输出变迁
	 */
	@Override
	public List<TransitionModel> getOutputs() {
		return Collections.emptyList();
	}
}
