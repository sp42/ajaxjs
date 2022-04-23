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
package com.ajaxjs.workflow;

/**
 * 框架抛出的所有异常都是此类（unchecked exception）
 * 
 * @author Frank Cheung
 *
 */
public class WorkflowException extends RuntimeException {
	private static final long serialVersionUID = -5220859421440167454L;

	public WorkflowException() {
		super();
	}

	public WorkflowException(String msg, Throwable cause) {
		super(msg);
		super.initCause(cause);
	}

	public WorkflowException(String msg) {
		super(msg);
	}

	public WorkflowException(Throwable cause) {
		super();
		super.initCause(cause);
	}
}
