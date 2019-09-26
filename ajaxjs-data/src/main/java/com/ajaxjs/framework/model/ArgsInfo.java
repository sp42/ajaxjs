/**
 * Copyright sp42 frank@ajaxjs.com
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
package com.ajaxjs.framework.model;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 修改后的信息
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ArgsInfo {
	public String sql;
	
	public Object[] args;
	
	/**
	 * 传入一个匿名函数，转化 sql
	 */
	public Function<String, String> sqlHandler;
	
	public Method method;
}
