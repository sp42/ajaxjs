package com.ajaxjs.workflow.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.model.po.Surrogate;
import org.springframework.stereotype.Component;

/**
 * 委托业务
 */
@Component
public class SurrogateService implements WfConstant {
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
        StringBuilder buffer = new StringBuilder(50);
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

    /**
     * 增加委托
     *
     * @param entity 委托
     */
    public void addSurrogate(Surrogate entity) {
        if (entity.getStat() == null)
            entity.setStat(1);

        CRUD.create(entity);
    }

    /**
     * 删除委托
     *
     * @param id ID
     */
    public void deleteSurrogate(Long id) {
        Surrogate entity = new Surrogate();
        entity.setId(id);

        CRUD.delete("wf_surrogate", id);
    }
}
