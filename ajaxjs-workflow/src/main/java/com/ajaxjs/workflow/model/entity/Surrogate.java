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
package com.ajaxjs.workflow.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托代理实体类
 * 
 */
public class Surrogate implements Serializable {
	private static final long serialVersionUID = -7359321877096338448L;

	/**
	 * 流程name
	 */
	private Date processName;

	/**
	 * 授权人
	 */
	private Long operator;

	/**
	 * 代理人
	 */
	private Long surrogate;

	/**
	 * 操作时间
	 */
	private Date operatorDate;

	/**
	 * 开始时间
	 */
	private Date startDate;

	/**
	 * 结束时间
	 */
	private Date endDate;

	public Date getProcessName() {
		return processName;
	}

	public void setProcessName(Date processName) {
		this.processName = processName;
	}

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public Long getSurrogate() {
		return surrogate;
	}

	public void setSurrogate(Long surrogate) {
		this.surrogate = surrogate;
	}

	public Date getOdate() {
		return operatorDate;
	}

	public void setOdate(Date odate) {
		this.operatorDate = odate;
	}

	public Date getSdate() {
		return startDate;
	}

	public void setSdate(Date sdate) {
		this.startDate = sdate;
	}

	public Date getEdate() {
		return endDate;
	}

	public void setEdate(Date edate) {
		this.endDate = edate;
	}

}
