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

import java.io.Serializable;

import com.ajaxjs.workflow.service.handler.IHandler;

/**
 * 模型元素基类
 * 
 */
public class BaseWfModel implements Serializable {
	private static final long serialVersionUID = 3082741431225739241L;

	/**
	 * 元素名称
	 */
	private String name;

	/**
	 * 显示名称
	 */
	private String displayName;

	/**
	 * 将执行对象 execution 交给具体的处理器处理
	 * TODO 或者取消？
	 * 
	 * @param handler   具体的处理器
	 * @param execution 执行对象
	 */
	protected void fire(IHandler handler, Execution execution) {
		handler.handle(execution);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
