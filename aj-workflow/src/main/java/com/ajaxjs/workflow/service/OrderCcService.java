package com.ajaxjs.workflow.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ajaxjs.my_data_framework.BaseService;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.model.po.OrderCc;

/**
 * 委托
 * @author Frank Cheung
 *
 */
@Component
public class OrderCcService extends BaseService<OrderCc> {
	/**
	 * 根据实例 id 查找抄送列表
	 * 
	 * @param orderId 流程实例 id
	 * @return 抄送列表
	 */
	public List<OrderCc> findByOrderId(Long orderId) {
		return findList(by("orderId", orderId));
	}

	/**
	 * 根据实例 id 和 参与者列表 查找抄送列表
	 * 
	 * @param orderId  流程实例 id
	 * @param actorIds 参与者 id 列表
	 * @return 抄送列表
	 */
	public List<OrderCc> findList(Long orderId, Long... actorIds) {
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
			OrderCc ccorder = new OrderCc();
			ccorder.setOrderId(orderId);
			ccorder.setCreator(creator);
			ccorder.setActorId(actorId);
			ccorder.setStat(WfConstant.STATE_ACTIVE);

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
		List<OrderCc> ccorders = findList(orderId, actorIds);
		Objects.nonNull(ccorders);

		for (OrderCc ccorder : ccorders) {
			ccorder.setStat(WfConstant.STATE_FINISH);
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
		List<OrderCc> ccorders = findList(orderId, actorId);

		for (OrderCc ccorder : ccorders)
			delete(ccorder);
	}
}
