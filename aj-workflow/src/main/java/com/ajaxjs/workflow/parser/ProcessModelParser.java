package com.ajaxjs.workflow.parser;

import java.util.Objects;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.XmlHelper;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.TransitionModel;

/**
 * 流程定义解析器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ProcessModelParser {
	/**
	 * 解析流程定义文件，并将解析后的对象放入模型容器中
	 * 
	 * @param processXml 流程定义的 XML
	 */
	public static ProcessModel parse(String processXml) {
		ProcessModel process = new ProcessModel();

		XmlHelper.parseXML(processXml, (el, nodeList) -> {
			process.setName(el.getAttribute(AbstractNodeParser.ATTR_NAME));
			process.setDisplayName(el.getAttribute(AbstractNodeParser.ATTR_DISPLAYNAME));
			process.setExpireDate(CommonUtil.Objet2Date(el.getAttribute(AbstractNodeParser.ATTR_EXPIRETIME)));
			process.setInstanceUrl(el.getAttribute(AbstractNodeParser.ATTR_INSTANCEURL));
			process.setInstanceNoClass(el.getAttribute(AbstractNodeParser.ATTR_INSTANCENOCLASS));

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					NodeModel model = parseModel(node);
					process.getNodes().add(model);
				}
			}

			// 循环节点模型，构造变迁输入、输出的 source、target
			for (NodeModel node : process.getNodes()) {
				for (TransitionModel transition : node.getOutputs()) {
					String to = transition.getTo();

					for (NodeModel node2 : process.getNodes()) {
						if (to.equalsIgnoreCase(node2.getName())) {
							node2.getInputs().add(transition);
							transition.setTarget(node2);
						}
					}
				}
			}
		});

		return process;
	}

	// context 是否已经装载 parser
	private static boolean isInited = false;

	/**
	 * 对流程定义 xml 的节点，根据其节点对应的解析器解析节点内容
	 * 
	 * @param node DOM 节点
	 * @return 解释结果是一个 Model，返回之
	 */
	private static NodeModel parseModel(Node node) {
		if (!isInited) {
			ComponentMgr.register("CustomParser", CustomParser.class, true);
			ComponentMgr.register("DecisionParser", DecisionParser.class, true);
			ComponentMgr.register("EndParser", EndParser.class, true);
			ComponentMgr.register("ForkParser", ForkParser.class, true);
			ComponentMgr.register("JoinParser", JoinParser.class, true);
			ComponentMgr.register("ProcessModelParser", ProcessModelParser.class, true);
			ComponentMgr.register("StartParser", StartParser.class, true);
			ComponentMgr.register("SubprocessParser", SubprocessParser.class, true);
			ComponentMgr.register("TaskParser", TaskParser.class, true);

			isInited = true;
		}

		String str = node.getNodeName() + "Parser";
		str = str.substring(0, 1).toUpperCase() + str.substring(1);

		Object p = ComponentMgr.get(str);
		Objects.requireNonNull(p, "不存在这类型的解释器 " + str);
		AbstractNodeParser nodeParser = (AbstractNodeParser) p;
		nodeParser.parse((Element) node);

		return nodeParser.model;
	}
}
