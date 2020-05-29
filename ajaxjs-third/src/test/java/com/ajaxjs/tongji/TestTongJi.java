package com.ajaxjs.tongji;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.net.http.HttpBasicRequest;

public class TestTongJi {

	@Test
	public void test() {
		String json = "{\"header\": "
				+ "{"
				+ "\"username\": \"sp42\", "
				+ "\"password\": \"a123123\","
				+ "\"token\": \"ced63a94f7e764d9d30cb92ceda67627\","
				+ "\"account_type\": 1"
				+ "}"
				+ "}";
		
		String html = HttpBasicRequest.post("https://api.baidu.com/json/tongji/v1/ReportService/getSiteList", json, conn -> {
			conn.addRequestProperty("Content-type", "application/json");
		});
		
		System.out.println(json);
		System.out.println(html);

		assertNotNull(html);
	}
}
