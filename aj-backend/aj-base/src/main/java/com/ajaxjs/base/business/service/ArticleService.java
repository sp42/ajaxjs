package com.ajaxjs.base.business.service;

import com.ajaxjs.base.business.model.Article;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;
import org.springframework.web.bind.annotation.*;

/**
 * 图文业务
 */
public interface ArticleService {
    @GetMapping("/{id}")
    @ControllerMethod("获取单笔图文")
    Article info(@PathVariable Long id);

    @GetMapping("/page")
    @ControllerMethod("分页获取图文列表")
    PageResult<Article> page();

    @PostMapping
    @ControllerMethod("创建图文")
    Long create(@RequestBody Article entity);

    @PutMapping
    @ControllerMethod("修改图文")
    Boolean update(@RequestBody Article entity);

    @DeleteMapping("/{id}")
    @ControllerMethod("删除单笔图文")
    Boolean delete(@PathVariable Long id);
}
