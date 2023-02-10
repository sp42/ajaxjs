package com.ajaxjs.user.sso;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import com.ajaxjs.user.sso.model.UserSession;
import com.ajaxjs.util.map.JsonHelper;

public class TestSsoClient {
	static String json = "{\"result\":{\"access_token\":\"1.f6b4dcee5baedd80d5a4b82ab51c47d66d977825.2592000.1653666272\",\"refresh_token\":\"2.9ab851fd578286b0065c55eaef0923b5629f43be.31536000.1682610272\",\"scope\":\"DEFAULT_SCOPE\",\"expires_in\":2592000,\"user\":{\"birthday\":\"2009-09-28 00:00:00\",\"stat\":null,\"updateDate\":\"2022-04-08 22:14:29\",\"address\":null,\"gender\":2,\"locationProvince\":null,\"roleId\":null,\"idCardNo\":null,\"jobTitle\":null,\"avatar\":null,\"content\":\"超级管理员\",\"uid\":null,\"extractData\":null,\"locationDistrict\":null,\"phone\":\"13711228150\",\"name\":\"admin\",\"verify\":null,\"location\":null,\"id\":1,\"email\":\"sp42@qq.com\",\"locationCity\":null,\"createDate\":\"2021-11-29 11:10:54\",\"status\":null,\"username\":\"admin\"}}}";

	@Test
	public void d() {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) JsonHelper.parseMap(json).get("result");

		UserSession saveSession = SsoClientController.saveSession(map);

		String accessToken = saveSession.accessToken.getAccessToken();
		System.out.println(accessToken);
		assertNotNull(accessToken);
	}
}
