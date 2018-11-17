package com.ajaxjs.cms.service;

import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.model.Feedback;
import com.ajaxjs.cms.service.FeedbackService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.dao.MockDataSource;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.mock.TestHelper;
import com.ajaxjs.orm.JdbcConnection;

public class TestFeedbackService {
	static FeedbackService service;

	@BeforeClass
	public static void initDb() {
		ConfigService.load("c:\\project\\wyzx-pc\\src\\resources\\site_config.json");
		JdbcConnection.setConnection(MockDataSource.getTestMySqlConnection(ConfigService.getValueAsString("testServer.mysql.url"), ConfigService.getValueAsString("testServer.mysql.user"),
				ConfigService.getValueAsString("testServer.mysql.password")));
		BeanContext.init("com.ajaxjs.cms");
		service = (FeedbackService) BeanContext.getBean("FeedbackService");
	}

	static String[] names = new String[] { "还可以，拿来系统的研究下", "操作很卡", "书质量还不错，送货速度也是刚刚的，赞一个。" };
	static String[] content = new String[] { "非常喜欢在京东购物，有时候上午下单，下午就到货了。快递小哥的整体素质也很高，这么热的天也送货上门。专业工具书看着不错，最近没活动，价格小贵！", 
			"看了一眼蛮好的，京东贼快贼给力，快递小哥也很棒", "整体还不错，包装也不错，就是运输过程外包装有点受影响。" };
	static String[] feedback = new String[] { null, "", "谢谢你的评价","这是我的电话 1333" };
	static int[] userIds = new int[] {4, 6, 7, 8 };

	@Test
	public void testCreate() {
		Feedback entity;
		
		for (int i = 0; i < 100; i++) {
			entity = new Feedback();
			entity.setName(TestHelper.getItem(names));
			entity.setContent(TestHelper.getItem(content));
			entity.setFeedback(TestHelper.getItem(feedback));
			entity.setCreatedByUser(TestHelper.getItem(userIds));
			entity.setEmail(TestHelper.getEmail(1, 4));
			entity.setPhone(TestHelper.getTel());
			assertNotNull(service.create(entity));
		}
	}

	@Test
	public void testPageList() {
		PageResult<Feedback> page;
		page = service.findPagedList( 0, 10);
		System.out.println(page.get(0).getUserName());
		assertNotNull(page.getTotalCount());
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
