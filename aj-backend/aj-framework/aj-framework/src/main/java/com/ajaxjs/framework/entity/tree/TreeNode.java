package com.ajaxjs.framework.entity.tree;

import com.ajaxjs.framework.Identity;

/**
 * 一个树节点
 */
public interface TreeNode extends Identity<Long> {
    default Long getParentId() {
        throw new RuntimeException();
    }

    /**
     * 设置是否叶子
     *
     * @param isLeaf 是否叶子
     */
    default void setIsLeaf(Boolean isLeaf) {
        throw new RuntimeException();
    }

    default Boolean isLeaf() {
        throw new RuntimeException();
    }
}
