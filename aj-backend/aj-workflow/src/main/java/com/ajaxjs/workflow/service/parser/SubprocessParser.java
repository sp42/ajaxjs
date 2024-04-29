package com.ajaxjs.workflow.service.parser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.node.work.SubProcessModel;

/**
 * 子流程节点解析类
 */
@Component
public class SubprocessParser extends AbstractNodeParser {
    @Override
    protected NodeModel newModel() {
        return new SubProcessModel();
    }

    @Value("${workflow.subProcessUrl}")
    private String subProcessUrl;

    /**
     * 解析节点信息，将 XML 元素中的数据加载到 SubProcessModel 中。
     *
     * @param node    表示待解析的节点，是一个 SubProcessModel 对象。
     * @param element 表示对应的 XML 元素，包含了节点的配置信息。
     */
    @Override
    protected void parseNode(NodeModel node, Element element) {
        // 将node转换为SubProcessModel类型，并设置其过程名称
        SubProcessModel model = (SubProcessModel) node;
        model.setProcessName(element.getAttribute(ATTR_PROCESS_NAME));

        // 从元素中获取版本号，并尝试将其解析为整数
        String version = element.getAttribute(ATTR_VERSION);
        int ver;

        try {
            ver = Integer.parseInt(version);
        } catch (NumberFormatException e) {
            ver = 0; // 如果版本号不是整数，则默认为0
        }

        model.setVersion(ver);

        // 获取表单信息，并设置到model中，如果表单信息为空，则使用默认的subProcessUrl
        String form = element.getAttribute(ATTR_FORM);
        model.setForm(StringUtils.hasText(form) ? form : subProcessUrl);
    }
}
