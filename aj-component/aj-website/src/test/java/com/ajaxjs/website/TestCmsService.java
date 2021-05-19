package com.ajaxjs.website;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.util.TestHelper;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.website.controller.HrController;
import com.ajaxjs.website.model.Ads;
import com.ajaxjs.website.model.Feedback;
import com.ajaxjs.website.service.AdsService;
import com.ajaxjs.website.service.FeedbackService;

public class TestCmsService {
	static FeedbackService FeedbackServicee;
	static AdsService AdsService;

	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();
		FeedbackServicee = ComponentMgr.get(FeedbackService.class);
		AdsService = ComponentMgr.get(AdsService.class);
	}

	static String[] feedbackContent = { "非常喜欢在京东购物，有时候上午下单，下午就到货了。快递小哥的整体素质也很高，这么热的天也送货上门。专业工具书看着不错，最近没活动，价格小贵！", "看了一眼蛮好的，京东贼快贼给力，快递小哥也很棒", "整体还不错，包装也不错，就是运输过程外包装有点受影响。" };
	static String[] feedbacks = { "这是我的电话 1333", "谢谢你的评价", "这是我的电话 1333" };
	static long[] userIds = { 4, 6, 7, 8 };

//	@Test
	public void testFeedbackServicee() {
		for (int i = 0; i < 10; i++) {
			Feedback feedback = new Feedback();
			feedback.setName(TestHelper.getChineseName());
			feedback.setContent(TestHelper.getItem(feedbackContent));
			feedback.setContact(TestHelper.getEmail(1, 4));
			feedback.setEmail(TestHelper.getEmail(1, 4));
			feedback.setPhone(TestHelper.getTel());
			feedback.setUserId(TestHelper.getItem(userIds));
			feedback.setFeedback(TestHelper.getItem(feedbacks));

			assertNotNull(FeedbackServicee.create(feedback));

			PageResult<Feedback> page;
			page = FeedbackServicee.findPagedList(0, 10);
			assertNotNull(page);
			assertNotNull(page.get(0));
			assertNotNull(page.get(0).getName());
			assertNotNull(page.getTotalCount());
		}
	}

	static String[] adsNames = { "首页广告", "内页广告" };

//	@Test
	public void testAdsService() {
		for (int i = 0; i < 10; i++) {
			Ads ads = new Ads();
			ads.setName(TestHelper.getItem(adsNames));
			ads.setCatalogId(25L);
			assertNotNull(AdsService.create(ads));
		}

		List<Ads> entities;
		entities = AdsService.findListByCatalogId(25);
		assertNotNull(entities.size());

		PageResult<Ads> pl = AdsService.findPagedList(0, 0, 5, 1, true);
		assertNotNull(pl);
	}

	static String[] names = { "招聘文员两名", "招聘会计一名" };
	static String[] content = { "1、监管应收账款、跟踪应收到期款；2、依据市场部订单进行应收账款的核算；3、工作认真,品行端正,吃苦耐劳", "1、负责部门一些日常行政事务,配合上级做好行政人事方面的工作；2、负责办理各类文件的收发、登记" };
	static String[] expr = { "一年", "两年", "三年" };

	@Test
	public void testHr() {
		for (int i = 0; i < 10; i++) {
			Map<String, Object> entity = new HashMap<>();
			entity.put("name", TestHelper.getItem(names));
			entity.put("content", TestHelper.getItem(content));
//			entity.put("intro", TestHelper.getItem(expr));
			assertNotNull(HrController.service.create(entity));
		}

		PageResult<Map<String, Object>> page = HrController.service.findPagedList(0, 10);
		assertNotNull(page.getTotalCount());
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
