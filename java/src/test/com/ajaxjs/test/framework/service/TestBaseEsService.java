package com.ajaxjs.test.framework.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.service.BaseEsService;

import crawler.model.Video;



public class TestBaseEsService {
	@Test
	public void testBaseESService() throws ServiceException {
		BaseEsService<Video> news = new BaseEsService<>();
		news.setReference(Video.class);
		news.setIndex("dept");
		news.setTableName("test");
		Video video = news.getById(1);
		System.out.println(video.getName());
		assertNotNull(video);
		
	}
}
