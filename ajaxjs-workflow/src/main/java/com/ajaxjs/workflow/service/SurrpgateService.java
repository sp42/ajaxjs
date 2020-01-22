package com.ajaxjs.workflow.service;

import java.util.List;
import java.util.Objects;

import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.helper.DateHelper;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.workflow.dao.SurrogateDao;
import com.ajaxjs.workflow.model.entity.Surrogate;

public class SurrpgateService extends BaseService<Surrogate> {
	{
		setUiName("委托");
		setShortName("surrogate");
		setDao(dao);
	}

	public static SurrogateDao dao = new Repository().bind(SurrogateDao.class);
	
	/**
	 * 根据授权人、流程名称获取最终代理人 如存在user1->user2->user3，那么最终返回user3
	 * 
	 * @param operator    授权人
	 * @param processName 流程名称
	 * @return String 代理人
	 */
	public String getSurrogate(Long operator, String processName) {
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
