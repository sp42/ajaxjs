package com.ajaxjs.net;

import org.junit.Test;

public class TesHttpBasicRequest {

	@Test
	public void testGet() {
//        String html = HttpBasicRequest.simpleGET(url), html2 = NetUtil.get(url);
//        assertEquals(html, html2);// 两种方法作用相同

//        String should400 = NetUtil.get("https://beta.bingolink.biz/iamapi/user/af38ddf7-dd53-4bad-bee9-0d81abefb817?access_token=bG9jYWw6RUxFQ2Y2aEZnY206R2VESE1SQmZtVQ");
//        System.out.println(should400);
//        Map<String, Object> map = ApiTool.getAsMap("https://beta.bingolink.biz/iamapi/user/af38ddf7-dd53-4bad-bee9-0d81abefb817?access_token=bG9jYWw6RUxFQ2Y2aEZnY206R2VESE1SQmZtVQ");
//        System.out.println("Map: " + map);

//		Object bean = ApiTool.get("https://beta.bingolink.biz/iamapi/user/af38ddf7-dd53-4bad-bee9-0d81abefb817?access_token=bG9jYWw6RUxFQ2Y2aEZnY206R2VESE1SQmZtVQ",
//				ErrorResult.class, ErrorResult.class);
//		System.out.println("Bean: " + ((ErrorResult) bean).getError());
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

	// @Test
//  public void combo() {
//      String html = NetUtil.get("https://gitee.com/sp42_admin/ajaxjs", true, conn -> {
//          HttpBasicRequest.setUserAgentDefault.accept(conn);
//      });
//
//      assertNotNull(html);
//  }
}
