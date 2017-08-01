package test.com.ajaxjs.util.collection;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.util.collection.JsonStruTraveler;
import com.ajaxjs.util.collection.JsonStruTraveler.TravelMapList_Iterator;

public class TestStruTravler {
	@Before
	public void init() {
		ConfigService.load();
	}

	@Test
	public void testMapListTravel() {
		JsonStruTraveler.travelMapList(ConfigService.config, new TravelMapList_Iterator() {
			@Override
			public void handler(String key, Object obj) {
				System.out.println(key + ":" + obj);
			}

			@Override
			public void newKey(String key) {
			}

			@Override
			public void exitKey(String key) {
			}
		});
	}

	@Test
	public void testFlatMap() {
		System.out.println(ConfigService.config);
		Map<String, Object> map = JsonStruTraveler.flatMap(ConfigService.config);
		System.out.println(map);
		assertNotNull(map);
	}
}
