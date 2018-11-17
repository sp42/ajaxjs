package com.ajaxjs.user.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.util.cryptography.SymmetricCipher;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.user.controller.filter.ApiAllowRequestCheck;

public class TestLoginCheck {
	@Before
	public void init() {
		ConfigService.load("c:\\project\\wyzx-pc\\src\\resources\\site_config.json");
	}
	
	@Test
	public void test() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		String token = SymmetricCipher.AES_Encrypt("token_" + new Date().getTime(), ConfigService.getValueAsString("Symmetric.AES_Key"));
		
//		MockFilter.hash.put("token", token);
		when(request.getHeader("token")).thenReturn(token);
		
		ApiAllowRequestCheck l = new ApiAllowRequestCheck();
		
		assertTrue(l.before(new MvcRequest(request), null, null));
	}
}
