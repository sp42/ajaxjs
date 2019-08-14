package com.ajaxjs.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.Version;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.util.map.JsonStruTraveler;

public class TestConfig {
	@Before
	public void init() {
		String file = Version.class.getClassLoader().getResource("resources/site_config.json").getPath();
		ConfigService.load(file);
	}
	
	@Test
	public void testConfig() {
		assertNotNull(ConfigService.config);

		JsonStruTraveler.flatMap(ConfigService.config);

		assertEquals(true, ConfigService.getValueAsBool("isDebug"));
		assertEquals("大华", ConfigService.getValueAsString("clientShortName"));
		assertEquals(15, ConfigService.getValueAsInt("data.articleCatalog_Id"));
	}

	@Test
	public void testTransform() {
		assertEquals("[\"clientShortName\"][\"bar\"][\"xx\"]", ConfigService.transform("clientShortName.bar.xx"));
	}

	@Test
	public void testLoadJSON_in_JS() {
		Map<String, Object> map = new HashMap<>();
		map.put("site.titlePrefix", "str");
		map.put("site.keywords", 1000);
		map.put("uploadFile.MaxTotalFileSize", 2);
		ConfigService.loadJSON_in_JS(map);
	}
}
