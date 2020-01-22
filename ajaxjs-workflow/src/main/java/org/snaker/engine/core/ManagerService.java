/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.snaker.engine.core;

import java.util.List;
import java.util.Objects;

import org.snaker.engine.IManagerService;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Surrogate;
import org.snaker.engine.helper.DateHelper;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.workflow.WorkflowUtils;

/**
 * 管理服务类
 * 
 * @author yuqs
 * @since 1.4
 */
public class ManagerService extends AccessService implements IManagerService {
	@Override
	public void saveOrUpdate(Surrogate surrogate) {
		Objects.requireNonNull(surrogate);
		surrogate.setState(STATE_ACTIVE);

		if (CommonUtil.isEmptyString(surrogate.getId())) {
			surrogate.setId(WorkflowUtils.getPrimaryKey());
			access().saveSurrogate(surrogate);
		} else
			access().updateSurrogate(surrogate);
	}

	@Override
	public void deleteSurrogate(String id) {
		Surrogate surrogate = getSurrogate(id);
		Objects.requireNonNull(surrogate);
		access().deleteSurrogate(surrogate);
	}

	@Override
	public Surrogate getSurrogate(String id) {
		return access().getSurrogate(id);
	}

	@Override
	public List<Surrogate> getSurrogate(QueryFilter filter) {
		Objects.requireNonNull(filter);
		return access().getSurrogate(null, filter);
	}

	@Override
	public List<Surrogate> getSurrogate(Page<Surrogate> page, QueryFilter filter) {
		Objects.requireNonNull(filter);
		return access().getSurrogate(page, filter);
	}

	/**
	 * 根据授权人、流程名称获取最终代理人 如存在user1->user2->user3，那么最终返回user3
	 * 
	 * @param operator    授权人
	 * @param processName 流程名称
	 * @return String 代理人
	 */
	@Override
	public String getSurrogate(String operator, String processName) {
		Objects.requireNonNull(operator);
		QueryFilter filter = new QueryFilter().setOperator(operator).setOperateTime(DateHelper.getTime());

		if (!CommonUtil.isEmptyString(processName))
			filter.setName(processName);

		List<Surrogate> surrogates = getSurrogate(filter);
		if (CommonUtil.isNull(surrogates))
			return operator;

		StringBuffer buffer = new StringBuffer(50);

		for (Surrogate surrogate : surrogates) {
			String result = getSurrogate(surrogate.getSurrogate(), processName);
			buffer.append(result).append(",");
		}

		buffer.deleteCharAt(buffer.length() - 1);

		return buffer.toString();
	}
}
