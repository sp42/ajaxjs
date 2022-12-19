package com.ajaxjs.data_service;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.TestConfig;
import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.entity.BaseEntityConstants;
import com.ajaxjs.util.TestHelper; 

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestSDK {
	@Autowired
	DataService dataService;

	interface ArticleDao extends IDataService<Article> {
	}

	public static final ArticleDao ArticleDAO = new Caller("cms", "cms_article").bind(ArticleDao.class, Article.class);

//	@Test
	public void test() {
		dataService.init();

	}

//	@Test
	public void testSDK() {
		Article article = ArticleDAO.findById(1L);
		TestHelper.printJson(article);
		System.out.println(article.getContent());
		assertNotNull(article);

	}

	@Test
	public void setNullValue() {
		Article article = new Article();
		article.setName("tetet");

		
	
		article.setCreateDate(BaseEntityConstants.NULL_DATE);

		Serializable id = ArticleDAO.create(article);
		System.out.println(id);

	}
}
