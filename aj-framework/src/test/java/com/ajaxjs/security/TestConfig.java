package com.ajaxjs.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.framework.config.EasyConfig;
import com.ajaxjs.util.cryptography.encryption.SymmetriCipher;

@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestConfig {
	@Autowired
	private EasyConfig config;

	@Test
	public void test() {
		String key = "FOOO";
		String e = SymmetriCipher.AES_Encrypt("MY_SECRET_WORDS", key);
		//@formatter:off  
		config.save("{\r\n" + 
				"	\"clientShortName\": \""+ e + "\",\r\n" + 
				"	\"FOO\": {\r\n" + 
				"		\"NUMBER\": 1221,\r\n" + 
				"		\"STR\": \"BAR22\",\r\n" + 
				"		\"BOOLEAN\": true,\r\n" + 
				"		\"NULL\": null,\r\n" + 
				"		\"ARRAY\": [\r\n" + 
				"			1,\r\n" + 
				"			\"STR\",\r\n" + 
				"			null\r\n" + 
				"		]\r\n" + 
				"	}\r\n" + 
				"}");
		//@formatter:on
		assertEquals("BAR22", config.getStr("FOO.STR"));
		assertEquals("24LLs0JsHC1VXc9UATaZyQ==", config.getStr("clientShortName"));
		assertEquals("MY_SECRET_WORDS", config.getStrDecrypt("clientShortName", key));
	}
}
