package com.ajaxjs.workflow.service.parser;

import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import com.ajaxjs.workflow.model.node.DecisionModel;
import com.ajaxjs.workflow.model.node.NodeModel;

/**
 * 决策节点解析类
 */
@Component
public class DecisionParser extends AbstractNodeParser {
    @Override
    protected NodeModel newModel() {
        return new DecisionModel();
    }

    @Override
    protected void parseNode(NodeModel node, Element element) {
        DecisionModel decision = (DecisionModel) node; // 解析 Decision 节点的特有属性 expr
        System.out.println("::::::::" + element.getAttribute(ATTR_EXPR));

        String v = element.getAttribute(ATTR_HANDLE_CLASS);
        decision.setExpr(element.getAttribute(ATTR_EXPR));
        decision.setHandleClass(element.getAttribute(ATTR_HANDLE_CLASS));
    }
}
