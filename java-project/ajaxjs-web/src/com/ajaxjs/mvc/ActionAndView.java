/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.mvc.controller.IController;

/**
 * 控制器的缓存
 * @author frank
 *
 */
public class ActionAndView {
	public IController controller;
	public Method GET_method;
	public Method POST_method;
	public Method PUT_method;
	public Method DELETE_method;
	public Method[] GET_methods;
	public Method[] POST_methods;
	public Method[] PUT_methods;
	public Method[] DELETE_methods;
	
	/**
	 * 子路径
	 */
	public Map<String, ActionAndView> subPath = new HashMap<>();
}
