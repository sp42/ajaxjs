package test.com.ajaxjs.config;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.config.ConfigService;

public class TestConfig {
	@Before
	public void init() {
		ConfigService.load();
	}

	@Test
	public void testConfig() {
		assertNotNull(ConfigService.config);
		
		assertEquals(true, ConfigService.getValueAsBool("isDebug"));
		assertEquals("大华", ConfigService.getValueAsString("clientShortName"));
		assertEquals(888, ConfigService.getValueAsInt("dfd.id"));
	}

 
}
