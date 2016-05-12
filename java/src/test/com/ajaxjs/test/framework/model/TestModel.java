package com.ajaxjs.test.framework.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.Entity;

public class TestModel {
	public static class News extends BaseModel {

	}

	public static class Article extends Entity {

	}

	@Test
	public void testBaseModel() {
		News news = new News();
		news.setName("Jack");
		assertNotNull(news);
		assertNotNull(news.getName());
	}

	@Test
	public void testEntity() {
		Article article = new Article();
		article.setName("article");
		assertNotNull(article);
		assertNotNull(article.getName());
	}
}
