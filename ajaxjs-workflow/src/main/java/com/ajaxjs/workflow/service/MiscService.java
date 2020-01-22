package com.ajaxjs.workflow.service;

import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.workflow.dao.MiscDao;

/**
 * 其他服务
 * @author Administrator
 *
 */
public class MiscService extends BaseService<Map<String, Object>> {
	{
		setUiName("其他查询");
		setShortName("surrogate");
		setDao(dao);
	}

	public static MiscDao dao = new Repository().bind(MiscDao.class);
	
//	/**
//	 * 创建抄送实例
//	 * 
//	 * @param orderId  流程实例id
//	 * @param actorIds 参与者id
//	 * @param creator  创建人id
//	 * @since 1.5
//	 */
//	void createCCOrder(String orderId, String creator, String... actorIds);
//	/**
//	 * 更新抄送记录为已阅
//	 * 
//	 * @param orderId  流程实例id
//	 * @param actorIds 参与者id
//	 */
//	void updateCCStatus(String orderId, String... actorIds);
//
//	/**
//	 * 删除抄送记录
//	 * 
//	 * @param orderId 流程实例id
//	 * @param actorId 参与者id
//	 */
//	void deleteCCOrder(String orderId, String actorId);
//
}
