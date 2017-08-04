package test.com.ajaxjs.util.collection;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.collection.JsonStruTraveler;
import com.ajaxjs.util.collection.JsonStruTraveler.TravelMapList_Iterator;

public class TestStruTravler {
	String j;
	List<Map<String, Object>> list;
	JsonStruTraveler t = new JsonStruTraveler();

	static String[] json = {"[ {",
	 "		\'name\' : \"关于我们\",",
	 "		\'id\' : \'about\',",
	 "		\'children\' : [ ",
	 "			{",
	 "				name : \"公司历程\",",
	 "				id : \'history\'",
	 "			},",
	 "			{",
	 "				name : \"企业文化\",",
	 "				id : \'cluture\'",
	 "			}",
	 "		]",
	 "	}, {",
	 "		\'name\' : \"美食天地\",",
	 "		\'id\' : \'product\',",
	 "		\'children\' : [ ",
	 "			{",
	 "				name : \"最新美食\",",
	 "				id : \'new\',",
	 "				\'children\' : [",
	 "					{",
	 "						\'id\' : \'yuecai\',",
	 "						\'name\' : \'粤菜\'",
	 "					},",
	 "					{",
	 "						\'id\' : \'xiangcai\',",
	 "						\'name\' : \'湘菜\'",
	 "					}",
	 "				]",
	 "			},",
	 "			{",
	 "				name : \"热门菜谱\",",
	 "				id : \'hot\'",
	 "			}",
	 "		]",
	 "	}]"};
	
	@Before
	public void init() {
		j = StringUtil.stringJoin(json, "");
		list = JsonHelper.parseList(j);
	}

	
	@Test
	public void testTravelList() {
		t.travelList(list);
		
		assertEquals(0, ((Map<String, Object>)list.get(0)).get("level"));
	}
	
	@Test
	public void testFindByPath() {
		assertEquals("关于我们", t.findByPath("about", list).get("name"));
		assertEquals("企业文化", t.findByPath("about/cluture", list).get("name"));
		assertEquals("粤菜", t.findByPath("product/new/yuecai", list).get("name"));
	}	
	
	String[] _map = {"{",
	 "	\"site\" : {",
	 "		\"titlePrefix\" : \"大华•川式料理\",",
	 "		\"keywords\" : \"大华•川式料理\",",
	 "		\"description\" : \"大华•川式料理饮食有限公司于2015年成立，本公司目标致力打造中国新派川菜系列。炜爵爷川菜料理系列的精髓在于清、鲜、醇、浓、香、烫、酥、嫩，擅用麻辣。在服务出品环节上，团队以ISO9000为蓝本建立标准化餐饮体系，务求以崭新的姿态面向社会各界人仕，提供更优质的服务以及出品。炜爵爷宗旨：麻辣鲜香椒，美味有诀窍，靓油用一次，精品煮御赐。 \",",
	 "		\"footCopyright\":\"dsds\" ",
	 "	},",
	 "	\"dfd\":{",
	 "		\"dfd\":\'fdsf\',",
	 "		\"id\": 888,",
	 "		\"dfdff\":{",
	 "			\"dd\":\'fd\'",
	 "		}",
	 "	},",
	 "	\"clientFullName\":\"大华•川式料理\",",
	 "	\"clientShortName\":\"大华\",",
	 "	\"isDebug\": true,",
	 "	\"data\" : {",
	 "		\"newsCatalog_Id\" : 6,",
	 "		\"jobCatalog_Id\" :7",
	 "	}",
	 "}"};
	
	Map<String, Object> map = JsonHelper.parseMap(StringUtil.stringJoin(_map, ""));
	
	@Test
	public void testMapListTravel() {
		JsonStruTraveler.travelMapList(map, new TravelMapList_Iterator() {
			@Override
			public void handler(String key, Object obj) {
				// System.out.println(key + ":" + obj);
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
Map<String, Object> f_map = JsonStruTraveler.flatMap(map);
assertEquals(7, f_map.get("data.jobCatalog_Id"));

JsonStruTraveler.travelMapList(f_map, new TravelMapList_Iterator() {
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
}
