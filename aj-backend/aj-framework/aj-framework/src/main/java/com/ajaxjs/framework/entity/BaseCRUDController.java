package com.ajaxjs.framework.entity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * CRUD 控制器
 */
public interface BaseCRUDController {
    /**
     * 单笔详情
     *
     * @param namespace 实体的命名空间
     * @param id        实体 id
     * @return 实体 Map
     */
    @GetMapping("/common/{namespace}/{id}")
    Map<String, Object> info(@PathVariable String namespace, @PathVariable Long id);

    /**
     * 实体列表
     *
     * @param namespace 实体的命名空间
     * @return 实体列表
     */
    @GetMapping("/common/{namespace}/list")
    List<Map<String, Object>> list(@PathVariable String namespace);
}
