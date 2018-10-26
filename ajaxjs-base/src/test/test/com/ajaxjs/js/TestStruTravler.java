package test.com.ajaxjs.js;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.js.JsonStruTraveler;
import com.ajaxjs.js.JsonStruTraveler.TravelMapList_Iterator;

public class TestStruTravler {
	JsonStruTraveler t = new JsonStruTraveler();
	
	@Before
	public void testTravelList() {
		t.travelList(TestJsonHelper.list);
	}
	
	@Test
	public void testFindByPath() {
		assertEquals("关于我们", t.findByPath("about", TestJsonHelper.list).get("name"));
		assertEquals("企业文化", t.findByPath("about/cluture", TestJsonHelper.list).get("name"));
		assertEquals("粤菜", t.findByPath("product/new/yuecai", TestJsonHelper.list).get("name"));
	}	
	
	@Test
	public void testMapListTravel() {
		JsonStruTraveler.travelMapList(TestJsonHelper.map, new TravelMapList_Iterator() {
			@Override
			public void handler(String key, Object obj) {
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
		Map<String, Object> f_map = JsonStruTraveler.flatMap(TestJsonHelper.map);
		assertEquals(new Double(7), f_map.get("data.jobCatalog_Id"));

		JsonStruTraveler.travelMapList(f_map, new TravelMapList_Iterator() {
			@Override
			public void handler(String key, Object obj) {
			}

			@Override
			public void newKey(String key) {
			}

			@Override
			public void exitKey(String key) {
			}
		});

	}
}
