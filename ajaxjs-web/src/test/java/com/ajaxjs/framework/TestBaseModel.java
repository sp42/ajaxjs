package com.ajaxjs.framework;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestBaseModel {
	@Test
	public void testEntity() {
		BaseModel news = new BaseModel();
		assertNotNull(news);
		news.setName("Test");
		assertNotNull(news.getName());
	}
}
