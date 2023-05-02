package com.ajaxjs.base.business.service;

import com.ajaxjs.base.business.model.Article;
import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.PageResult;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Override
    public Article info(Long id) {
        String sql = "SELECT * FROM article WHERE id = ?";
        return CRUD.info(Article.class, sql, id);
    }

    @Override
    public PageResult<Article> page() {
        return null;
    }

    @Override
    public Long create(Article entity) {
        return CRUD.create(entity);
    }

    @Override
    public Boolean update(Article entity) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }
}
