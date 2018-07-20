package test.com.ajaxjs.config;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.js.JsonStruTraveler;

public class TestConfig {
	@Before
	public void init() {
		ConfigService.load();
	}

	@Test
	public void testConfig() {
		assertNotNull(ConfigService.config);

		JsonStruTraveler.flatMap(ConfigService.config);

		assertEquals(true, ConfigService.getValueAsBool("isDebug"));
		assertEquals("大华", ConfigService.getValueAsString("clientShortName"));
		assertEquals(888, ConfigService.getValueAsInt("dfd.id"));
	}

	@Test
	public void testTransform() {
		assertEquals("[\"clientShortName\"][\"bar\"][\"xx\"]", ConfigService.transform("clientShortName.bar.xx"));
	}

	@Test
	public void testLoadJSON_in_JS() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("site.titlePrefix", "str");
		map.put("site.keywords", 1000);
		map.put("uploadFile.MaxTotalFileSize", 2);
		ConfigService.loadJSON_in_JS(map);
	}
}
