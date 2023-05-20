package com.ajaxjs.data.entity;

public class CrudUtils {
    /**
     * 检查是否有实体 id 字段提交
     *
     * @param entity 实体
     */
    public static void checkId(Identity<Long> entity) {
        if (entity.getId() == null || entity.getId() == 0)
            throw new IllegalArgumentException("缺少实体 id 字段提交");
    }
}
