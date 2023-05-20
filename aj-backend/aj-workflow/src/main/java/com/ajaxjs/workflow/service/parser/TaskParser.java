package com.ajaxjs.workflow.service.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ajaxjs.util.date.DateUtil;
import com.ajaxjs.workflow.model.FieldModel;
import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.node.work.TaskModel;

/**
 * 任务节点解析类
 */
@Component
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
		task.setExpireTime(DateUtil.object2Date(element.getAttribute(ATTR_EXPIRE_TIME)));
		task.setAutoExecute(element.getAttribute(ATTR_AUTO_EXECUTE));
		task.setCallback(element.getAttribute(ATTR_CALLBACK));
		task.setReminderTime(DateUtil.object2Date(element.getAttribute(ATTR_REMINDER_TIME)));
		task.setReminderRepeat(element.getAttribute(ATTR_REMINDER_REPEAT));
		task.setPerformType(element.getAttribute(ATTR_PERFORM_TYPE));
		task.setTaskType(element.getAttribute(ATTR_TASK_TYPE));
//		task.setAssignmentHandler(element.getAttribute(ATTR_ASSIGNEE_HANDLER));// TODO 指向一个 lambda？

		NodeList fieldList = element.getElementsByTagName(ATTR_FIELD);
		List<FieldModel> fields = new ArrayList<>();

		for (int i = 0; i < fieldList.getLength(); i++) {
			Element item = (Element) fieldList.item(i);
			FieldModel fieldModel = new FieldModel();
			fieldModel.setName(item.getAttribute(ATTR_NAME));
			fieldModel.setDisplayName(item.getAttribute(ATTR_DISPLAY_NAME));
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
