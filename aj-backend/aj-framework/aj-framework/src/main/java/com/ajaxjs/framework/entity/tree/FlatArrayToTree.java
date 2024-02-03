package com.ajaxjs.framework.entity.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一维列表转换为 Tree 结构
 * 扁平化的列表转换为 Tree 结构
 */
public class FlatArrayToTree extends BaseTreeStrut {

    @SuppressWarnings("unchecked")
    public <T extends Serializable> List<Map<String, Object>> mapAsTree(Class<T> idType, List<Map<String, Object>> nodes) {
        // 扁平化的列表转换为 tree 结构
        Map<T, Map<String, Object>> parents = new HashMap<>();

        for (Map<String, Object> node : nodes)
            parents.put((T) node.get(getIdField()), node);

        List<Map<String, Object>> tree = new ArrayList<>();

        for (Map<String, Object> node : nodes) {
            T parentId = (T) node.get(getParentIdField());

            if (!parentId.equals(getTopNodeValue())) {
                Map<String, Object> parent = parents.get(parentId);
                Object _children = parent.get(getChildrenField());
                List<Map<String, Object>> children;

                if (_children == null) {
                    children = new ArrayList<>();
                    parent.put(getChildrenField(), children);
                } else
                    children = (List<Map<String, Object>>) _children;

                children.add(node);
            } else tree.add(node);
        }

        return tree;
    }
}
