/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.ioc;

/**
 * 如果前置方法 before 返回该实例则表示在 before之后中止 aop
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class ReturnBefore {
	/**
	 * 保存返回值
	 */
	private Object returnValue;

	/**
	 * 创建一个返回值
	 * 
	 * @param returnValue 返回值
	 */
	public ReturnBefore(Object returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * 获取返回值
	 * 
	 * @return 返回值
	 */
	public Object getReturnValue() {
		return returnValue;
	}
}
