/**
 * Copyright 2015 sp42 frank@ajaxjs.com
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
package com.ajaxjs.mvc.controller;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.util.TreeLinked;

/**
 * A action = controller + methods
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class Action extends TreeLinked<Action> {
	/**
	 * 完整路径
	 */
	public String path;

	/**
	 * 控制器实例，方便反射时候调用（如果 HTTP 对应控制器没有，则读取这个）
	 */
	public IController controller;

	public IController getMethodController;

	public IController postMethodController;

	public IController putMethodController;

	public IController deleteMethodController;

	/**
	 * 该路径的 get 请求时对应的控制器方法
	 */
	public Method getMethod;

	/**
	 * 该路径的 get 请求时对应的控制器方法
	 */
	public Method postMethod;

	/**
	 * 该路径的 put 请求时对应的控制器方法
	 */
	public Method putMethod;

	/**
	 * 该路径的 delete 请求时对应的控制器方法
	 */
	public Method deleteMethod;
}
