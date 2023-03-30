package com.ajaxjs.entity;

import com.ajaxjs.framework.TestHelper;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 常见树结构的方法
 */
@Data
public class TreeCommonService {
    private String pidField = "pId";

    /**
     * Map 里面每个元素是树节点，都有id、pid 字段，pid 是指向父亲节点，
     * 如何求得这个节点下面没有 child，即 isLeaf = true，并将这 isLeaf 设置到 map
     *
     * @param tree 树
     */
    public void setLeafFlag(List<Map<String, Object>> tree) {
        setLeafFlag(tree, "isLeaf");
    }

    /**
     * Map 里面每个元素是树节点，都有id、pid 字段，pid 是指向父亲节点，
     * 如何求得这个节点下面没有 child，即 isLeaf = true，并将这 isLeaf 设置到 map
     *
     * @param tree        树
     * @param isLeafField 是否叶子的字段名
     */
    public void setLeafFlag(List<Map<String, Object>> tree, String isLeafField) {
        // 如果一个节点的 id在 parentIds 集合中找不到对应的元素，则可以认为该节点是叶子节点，因为没有其他节点引用它
        Set<Object> parentIds = new HashSet<>();

        for (Map<String, Object> node : tree)
            parentIds.add(node.get(pidField));

        for (Map<String, Object> node : tree) {
            boolean contains = parentIds.contains(node.get("id"));

            node.put(isLeafField, !contains);

        }

//        System.out.println(tree);


    }
}
