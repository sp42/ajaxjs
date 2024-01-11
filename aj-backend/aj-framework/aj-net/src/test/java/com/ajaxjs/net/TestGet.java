package com.ajaxjs.net;

import com.ajaxjs.net.http.Get;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestGet {
//	@Test
	public void testSimpleGet() {
		String html = Get.simpleGET("https://www.baidu.com");
		assertTrue(html.contains("百度一下，你就知道"));
	}

//	@Test
	public void testGet() {
		String html = Get.get("https://www.baidu.com").toString();
		assertTrue(html.contains("百度一下，你就知道"));
	}

//	@Test
	public void testDownload2disk() throws IOException {
		assertNotNull(Get.download("https://www.baidu.com/", "c:/temp"));
		String url = "https://bbsimage.res.meizu.com/forum/2019/01/23/153122zrz85kuvubbiibbs.jpg";
		assertNotNull(Get.download(url, "c:/temp"));
	}

	@Test
	public void testApi() {
		Map<String, Object> map = Get.api("https://beta.bingolink.biz/iamapi/user/af38ddf7-dd53-4bad-bee9-0d81abefb817?access_token=bG9jYWw6RUxFQ2Y2aEZnY206R2VESE1SQmZtVQ");
		System.out.println(map);
	}

	/**
	 * OAuth2 标准接口统一错误响应规范
	 *
	 * @author Frank Cheung
	 */
	public static class ErrorResult {
		/**
		 * HTTP 响应状态码
		 */
		private Integer http_code;

		/**
		 * 错误码，按照标准oauth2定义
		 */
		private String error;

		/**
		 * 错误代码，具体的错误原因代码(扩展)
		 */
		private String error_description;

		public Integer getHttp_code() {
			return http_code;
		}

		public void setHttp_code(Integer http_code) {
			this.http_code = http_code;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getError_description() {
			return error_description;
		}

		public void setError_description(String error_description) {
			this.error_description = error_description;
		}
	}
}
