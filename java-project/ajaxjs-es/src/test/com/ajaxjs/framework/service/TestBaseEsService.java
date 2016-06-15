package test.com.ajaxjs.framework.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.service.BaseEsService;
import com.egdtv.crawler.model.Video;

public class TestBaseEsService {
	@Test
	public void testBaseESService() throws ServiceException {
		BaseEsService<Video> news = new BaseEsService<Video>();
		news.setReference(Video.class);
		news.setIndex("dept");
		news.setTableName("test");
		Video video = news.getById(2539);
		System.out.println(video.getName());
		assertNotNull(video);
		
	}
}
