package com.ajaxjs.workflow.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.workflow.model.FieldModel;
import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.TaskModel;

/**
 * 任务节点解析类
 * 
 */
public class TaskParser extends AbstractNodeParser {
	@Override
	protected NodeModel newModel() {
		return new TaskModel();
	}

	/**
	 * 由于任务节点需要解析 form、assignee 属性，这里覆盖抽象类方法实现
	 */
	@Override
	protected void parseNode(NodeModel node, Element element) {
		TaskModel task = (TaskModel) node;
		task.setForm(element.getAttribute(ATTR_FORM));
		task.setAssignee(element.getAttribute(ATTR_ASSIGNEE));
		task.setExpireTime(CommonUtil.Objet2Date(element.getAttribute(ATTR_EXPIRETIME)));
		task.setAutoExecute(element.getAttribute(ATTR_AUTOEXECUTE));
		task.setCallback(element.getAttribute(ATTR_CALLBACK));
		task.setReminderTime(CommonUtil.Objet2Date(element.getAttribute(ATTR_REMINDERTIME)));
		task.setReminderRepeat(element.getAttribute(ATTR_REMINDERREPEAT));
		task.setPerformType(element.getAttribute(ATTR_PERFORMTYPE));
		task.setTaskType(element.getAttribute(ATTR_TASKTYPE));
//		task.setAssignmentHandler(element.getAttribute(ATTR_ASSIGNEE_HANDLER));// TODO 指向一个 lambda？

		NodeList fieldList = element.getElementsByTagName(ATTR_FIELD);
		List<FieldModel> fields = new ArrayList<>();

		for (int i = 0; i < fieldList.getLength(); i++) {
			Element item = (Element) fieldList.item(i);
			FieldModel fieldModel = new FieldModel();
			fieldModel.setName(item.getAttribute(ATTR_NAME));
			fieldModel.setDisplayName(item.getAttribute(ATTR_DISPLAYNAME));
			fieldModel.setType(item.getAttribute(ATTR_TYPE));
			NodeList attrList = item.getElementsByTagName(ATTR_ATTR);

			for (int j = 0; j < attrList.getLength(); j++) {
				Node attr = attrList.item(j);
				fieldModel.addAttr(((Element) attr).getAttribute(ATTR_NAME), ((Element) attr).getAttribute(ATTR_VALUE));
			}

			fields.add(fieldModel);
		}

		task.setFields(fields);
	}

}
