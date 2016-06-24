package test.com.ajaxjs.framework.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import test.com.ajaxjs.framework.model.News;


public class TestService {
	@Test
	public void testService() {
		News news = new News();
		assertNotNull(news);
		
	}
}
