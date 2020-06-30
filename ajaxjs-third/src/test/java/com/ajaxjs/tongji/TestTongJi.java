package com.ajaxjs.tongji;

import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.http.HttpBasicRequest;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.map.JsonHelper;

public class TestTongJi {

//	@Test
	public void test() {
		String json = "{\"header\": " + "{" + "\"username\": \"sp42\", " + "\"password\": \"a123123\"," + "\"token\": \"ced63a94f7e764d9d30cb92ceda67627\"," + "\"account_type\": 1"
				+ "}" + "}";

		String html = HttpBasicRequest.post("https://api.baidu.com/json/tongji/v1/ReportService/getSiteList", json, conn -> {
			conn.addRequestProperty("Content-type", "application/json");
		});

		System.out.println(json);
		System.out.println(html);

		assertNotNull(html);
	}

	private final static SimpleDateFormat formater = CommonUtil.simpleDateFormatFactory("yyyyMMdd");

	@Test
	public void testPV() {
		ConfigService.load("E:\\project\\aj-website\\WebContent\\META-INF\\site_config.json");
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);// 昨天

		String today = formater.format(now), yesterday = formater.format(calendar.getTime());

		Map<String, Object> header = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("username", ConfigService.get("baidu_tongji.api_username"));
				put("password", ConfigService.get("baidu_tongji.api_password"));
				put("token", ConfigService.get("baidu_tongji.api_token"));
				put("account_type", 1);
			}
		}, body = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("site_id", ConfigService.get("baidu_tongji.siteId"));
				put("start_date", today);
				put("end_date", yesterday);
				put("method", "trend/time/a");
				put("metrics", "pv_count,visit_count");
				put("max_results", "0");
				put("gran", "day");
			}
		};

		String json = String.format("{\"header\": %s, \"body\": %s}", JsonHelper.stringifyMap(header), JsonHelper.stringifyMap(body));

		String html = HttpBasicRequest.post("https://api.baidu.com/json/tongji/v1/ReportService/getData", json, conn -> {
			conn.addRequestProperty("Content-type", "application/json");
		});

		System.out.println(html);

		assertNotNull(html);
	}
}
