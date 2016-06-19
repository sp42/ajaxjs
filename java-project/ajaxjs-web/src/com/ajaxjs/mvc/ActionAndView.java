/*
 * Copyright 2009 Pavel Jbanov
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ajaxjs.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ActionAndView {
	private Class<?> handlerClass;
	private Pattern pattern;
	private String methodName;

	private Map<String, Action> result2action = new HashMap<>();

	public ActionAndView(String regex, Class<?> handlerClass) {
		pattern = Pattern.compile(regex);
		this.handlerClass = handlerClass;
	}

	public ActionAndView(String regex, Class<?> handlerClass, String methodName) {
		pattern = Pattern.compile(regex);
		this.handlerClass = handlerClass;
		this.methodName = methodName;
	}

	public ActionAndView dispatchResult(String result, String location) {
		result2action.put(result, new DispatcherAction(location));
		return this;
	}

	public ActionAndView customResult(String result, Action action) {
		result2action.put(result, action);
		return this;
	}

	public Action getAction(String result) {
		return result2action.get(result);
	}
	
	
	///////////////////////////////////////////////////////////

	public Class<?> getHandlerClass() {
		return handlerClass;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getMethodName() {
		return methodName;
	}
}
