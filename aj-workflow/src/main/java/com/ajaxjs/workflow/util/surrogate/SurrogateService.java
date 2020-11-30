package com.ajaxjs.workflow.util.surrogate;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;

@Component
public class SurrogateService extends BaseService<Surrogate> {
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
	public Long getSurrogate(Long operator, String processName) {
//		QueryFilter filter = new QueryFilter().setOperator(operator).setOperateTime(DateHelper.getTime());
//
//		if (!CommonUtil.isEmptyString(processName))
//			filter.setName(processName);
//
//		List<Surrogate> surrogates = getSurrogate(filter);
//		if (CommonUtil.isNull(surrogates))
//			return operator;
//
		StringBuffer buffer = new StringBuffer(50);
//
//		for (Surrogate surrogate : surrogates) {
//			String result = getSurrogate(surrogate.getSurrogate(), processName);
//			buffer.append(result).append(",");
//		}
//
		buffer.deleteCharAt(buffer.length() - 1);
//
//		return buffer.toString();
		return null;
	}
}