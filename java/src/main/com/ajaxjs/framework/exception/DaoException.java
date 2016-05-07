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
package com.ajaxjs.framework.exception;

/**
 * 数据操作异常
 * 
 * @author frank
 *
 */
public class DaoException extends ServiceException {
	/**
	 * 创建一个数据操作异常对象。
	 * 
	 * @param message
	 *            错误信息提示
	 */
	public DaoException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
