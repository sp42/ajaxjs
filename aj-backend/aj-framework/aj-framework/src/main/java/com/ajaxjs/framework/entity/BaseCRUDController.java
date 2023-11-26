package com.ajaxjs.framework.entity;

import com.ajaxjs.framework.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/{namespace}/{id}")
    Map<String, Object> info(@PathVariable String namespace, @PathVariable Long id);

    /**
     * 实体列表
     *
     * @param namespace 实体的命名空间
     * @return 实体列表
     */
    @GetMapping("/{namespace}/list")
    List<Map<String, Object>> list(@PathVariable String namespace);

    /**
     * 分页获取实体列表
     *
     * @param namespace 实体的命名空间
     * @return 实体列表
     */
    @GetMapping("/{namespace}/page")
    PageResult<Map<String, Object>> page(@PathVariable String namespace);

    @PostMapping("/{namespace}/create")
    Long create(@PathVariable String namespace, @RequestParam Map<String, Object> params);

    @PostMapping("/{namespace}/update")
    Boolean update(@PathVariable String namespace, @RequestParam Map<String, Object> params);

    @PostMapping("/{namespace}/delete/{id}")
    Boolean delete(@PathVariable String namespace, @PathVariable Long id);

    /**
     * 重新加载数据库的配置
     *
     * @return 是否成功
     */
    @GetMapping("/reload_config")
    boolean reloadConfig();
}
