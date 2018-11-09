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
 * AOP 专用异常信息
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class AopException extends Exception {
	private static final long serialVersionUID = 5423010532958020537L;

	/**
	 * 创建一个 AOP 专用异常信息
	 * 
	 * @param msg
	 *            异常信息
	 * @param type
	 *            是前置还是后置？ before|after
	 */
	public AopException(String msg, String type) {
		super(msg);
	}
}
