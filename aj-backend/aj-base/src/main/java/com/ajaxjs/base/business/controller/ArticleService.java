package com.ajaxjs.base.business.controller;

import com.ajaxjs.base.business.model.Article;
import com.ajaxjs.framework.PageResult;
import org.springframework.web.bind.annotation.*;

/**
 * 图文业务
 */
public interface ArticleService {
    /**
     * 获取单笔图文
     *
     * @param id
     * @return 单笔图文
     */
    @GetMapping("/{id}")
    Article info(@PathVariable Long id);

    /**
     * 分页获取图文列表
     *
     * @return 图文列表
     */
    @GetMapping("/page")
    PageResult<Article> page();

    /**
     * 创建图文
     *
     * @param entity
     * @return
     */
    @PostMapping
    Long create(@RequestBody Article entity);

    /**
     * 修改图文
     *
     * @param entity 图文
     * @return
     */
    @PutMapping
    Boolean update(@RequestBody Article entity);

    /**
     * 删除单笔图文
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    Boolean delete(@PathVariable Long id);
}
