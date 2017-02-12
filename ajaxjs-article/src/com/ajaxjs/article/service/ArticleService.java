package com.ajaxjs.article.service;

import com.ajaxjs.article.dao.ArticleDAO;
import com.ajaxjs.article.model.Article;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.service.BaseCrudService;

public class ArticleService extends BaseCrudService<Article, ArticleDAO> {
	public ArticleService() {
		setMapper(ArticleDAO.class);
		setTableName("Article");
		setUiName("新闻");
//		setReference(Article.class);
	}
	
	@Override
	public Article getById(long id) throws ServiceException {
		Article bean = super.getById(id);
		
		if(bean != null && getModel() != null) {
//			Map<String, Object> model = getModel();
//			Common.getLinkDataForInfo(bean, model);
		}
		
		return bean;
	}

	
}
