package test.com.ajaxjs.framework.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.business.model.News;


public class TestService {
	@Test
	public void testService() {
		News news = new News();
		assertNotNull(news);
		
	}
}
