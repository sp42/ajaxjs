package com.ajaxjs.workflow.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.workflow.WorkflowConstant;
import com.ajaxjs.workflow.dao.CCOrderDao;
import com.ajaxjs.workflow.model.entity.CCOrder;

/**
 * 委托
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@Component
public class CCOrderService extends BaseService<CCOrder> {
	{

		setDao(dao);
	}

	public static CCOrderDao dao = new Repository().bind(CCOrderDao.class);

	/**
	 * 根据实例 id 查找抄送列表
	 * 
	 * @param orderId 流程实例 id
	 * @return 抄送列表
	 */
	public List<CCOrder> findByOrderId(Long orderId) {
		return findList(by("orderId", orderId));
	}

	/**
	 * 根据实例 id 和 参与者列表 查找抄送列表
	 * 
	 * @param orderId  流程实例 id
	 * @param actorIds 参与者 id 列表
	 * @return 抄送列表
	 */
	public List<CCOrder> findList(Long orderId, Long... actorIds) {
		return findList(by("orderId", orderId));
	}

	/**
	 * 创建抄送实例
	 * 
	 * @param orderId  流程实例 id
	 * @param creator  创建人 id
	 * @param actorIds 参与者 id 列表
	 */
	public void createCCOrder(Long orderId, Long creator, Long... actorIds) {
		for (Long actorId : actorIds) {
			CCOrder ccorder = new CCOrder();
			ccorder.setOrderId(orderId);
			ccorder.setCreator(creator);
			ccorder.setActorId(actorId);
			ccorder.setStat(WorkflowConstant.STATE_ACTIVE);

			create(ccorder);
		}
	}

	/**
	 * 更新抄送记录为已阅
	 * 
	 * @param orderId  流程实例 id
	 * @param actorIds 参与者 id 列表
	 */
	public void updateCCStatus(Long orderId, Long... actorIds) {
		List<CCOrder> ccorders = findList(orderId, actorIds);
		Objects.nonNull(ccorders);

		for (CCOrder ccorder : ccorders) {
			ccorder.setStat(WorkflowConstant.STATE_FINISH);
			ccorder.setFinishTime(new Date());
			update(ccorder);
		}
	}

	/**
	 * 删除指定的抄送记录
	 * 
	 * @param orderId 流程实例 id
	 * @param actorId 参与者 id
	 */
	public void deleteCCOrder(Long orderId, Long actorId) {
		List<CCOrder> ccorders = findList(orderId, actorId);

		for (CCOrder ccorder : ccorders)
			delete(ccorder);
	}
}
