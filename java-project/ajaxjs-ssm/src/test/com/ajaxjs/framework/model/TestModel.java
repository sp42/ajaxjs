package test.com.ajaxjs.framework.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import test.com.ajaxjs.framework.business.News;


public class TestModel {


	@Test
	public void testBaseModel() {
		News news = new News();
		news.setName("Jack");
		assertNotNull(news);
		assertNotNull(news.getName());
	}

	@Test
	public void testEntity() {
//		Article article = new Article();
//		article.setName("article");
//		assertNotNull(article);
//		assertNotNull(article.getName());
	}
}
