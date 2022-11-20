package com.ajaxjs.workflow.model.po;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.workflow.common.WfConstant;

/**
 * 持久对象
 * 
 * PO 就是数据库中的记录，一个PO的数据结构对应着库中表的结构，表中的一条记录就是一个 PO 对象
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public abstract class WfPersistantObject extends BaseModel implements IBaseModel, WfConstant {

}
