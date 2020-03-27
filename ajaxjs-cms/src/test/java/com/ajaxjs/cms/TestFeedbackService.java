package com.ajaxjs.cms;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mock.TestHelper;
import com.ajaxjs.orm.JdbcConnection;

public class TestFeedbackService {
	static FeedbackService service;

	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		service = (FeedbackService) BeanContext.getBean("FeedbackService");
	}

	static String[] names = new String[] { "还可以，拿来系统的研究下", "操作很卡", "书质量还不错，送货速度也是刚刚的，赞一个。" };
	static String[] content = new String[] { "非常喜欢在京东购物，有时候上午下单，下午就到货了。快递小哥的整体素质也很高，这么热的天也送货上门。专业工具书看着不错，最近没活动，价格小贵！", "看了一眼蛮好的，京东贼快贼给力，快递小哥也很棒", "整体还不错，包装也不错，就是运输过程外包装有点受影响。" };
	static String[] feedback = new String[] { null, "", "谢谢你的评价", "这是我的电话 1333" };
	static int[] userIds = new int[] { 4, 6, 7, 8 };

//	@Test
	public void testCreate() {
		for (int i = 0; i < 10; i++) {
			Map<String, Object> entity = new HashMap<>();
			entity.put("name", TestHelper.getItem(names));
			entity.put("content", TestHelper.getItem(content));
			entity.put("feedback", TestHelper.getItem(feedback));
			entity.put("userIds", TestHelper.getItem(userIds));
			entity.put("email", TestHelper.getEmail(1, 4));
			entity.put("phone", TestHelper.getTel());
			assertNotNull(service.create(entity));
		}
	}

	@Test
	public void testPageList() {
		PageResult<Map<String, Object>> page;
		page = service.findPagedList(0, 10);
		assertNotNull(page);
//		assertNotNull(page.get(0));
//		assertNotNull(page.get(0).get("name"));
//		assertNotNull(page.getTotalCount());
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
