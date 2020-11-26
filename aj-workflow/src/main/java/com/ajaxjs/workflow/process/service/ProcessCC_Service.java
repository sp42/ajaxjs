package com.ajaxjs.workflow.process.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.process.ProcessCC;

@Component
public class ProcessCC_Service extends BaseService<ProcessCC> {
	{
		setUiName("抄送");
		setShortName("process_cc");
		setDao(DAO);
	}

	@TableName(value = "wf_process_cc", beanClass = ProcessCC.class)
	private static interface ProcessCC_Dao extends IBaseDao<ProcessCC> {
	}

	private static ProcessCC_Dao DAO = new Repository().bind(ProcessCC_Dao.class);

	/**
	 * 根据实例 id 查找抄送列表
	 * 
	 * @param activeId 流程实例 id
	 * @return 抄送列表
	 */
	public List<ProcessCC> findByActiveId(Long activeId) {
		return findList(by("activeId", activeId));
	}

	/**
	 * 根据实例 id 和 参与者列表 查找抄送列表 TODO
	 * 
	 * @param activeId 流程实例 id
	 * @param actorIds 参与者 id 列表
	 * @return 抄送列表
	 */
	public List<ProcessCC> findList(Long activeId, Long... actorIds) {
		return findList(by("activeId", activeId));
	}

	/**
	 * 创建抄送实例
	 * 
	 * @param activeId 流程实例 id
	 * @param creator  创建人 id
	 * @param actorIds 参与者 id 列表
	 */
	public void create(Long activeId, Long creator, Long... actorIds) {
		for (Long actorId : actorIds) {
			ProcessCC ccorder = new ProcessCC();
			ccorder.setActiveId(activeId);
			ccorder.setCreator(creator);
			ccorder.setActorId(actorId);
			ccorder.setStat(ProcessModel.STATE_ACTIVE);

			create(ccorder);
		}
	}

	/**
	 * 更新抄送记录为已阅
	 * 
	 * @param activeId 流程实例 id
	 * @param actorIds 参与者 id 列表
	 */
	public void update(Long activeId, Long... actorIds) {
		List<ProcessCC> ccorders = findList(activeId, actorIds);
		Objects.nonNull(ccorders);

		for (ProcessCC ccorder : ccorders) {
			ccorder.setStat(ProcessModel.STATE_FINISH);
			ccorder.setFinishTime(new Date());
			update(ccorder);
		}
	}

	/**
	 * 删除指定的抄送记录
	 * 
	 * @param activeId 流程实例 id
	 * @param actorId  参与者 id
	 */
	public void delete(Long activeId, Long actorId) {
		List<ProcessCC> ccorders = findList(activeId, actorId);

		for (ProcessCC ccorder : ccorders)
			delete(ccorder);
	}
}
