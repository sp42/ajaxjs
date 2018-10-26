package test.com.ajaxjs.js;

import static com.ajaxjs.js.JsonHelper.format;
import static com.ajaxjs.js.JsonHelper.parseMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.js.JsonHelper;

public class TestJsonHelper {
	static String[] _map = {"{",
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
			 "	\"clientFullName\":88,",
			 "	\"clientShortName\":\"大华\",",
			 "	\"isDebug\": true,",
			 "	\"data\" : {",
			 "		\"newsCatalog_Id\" : 6,",
			 "		\"jobCatalog_Id\" :7",
			 "	}",
			 "}"};
			
	public static Map<String, Object> map = parseMap(String.join("", _map));

	@Test
	public void testParseMap() {
		assertEquals("大华", map.get("clientShortName").toString());
		assertEquals(88, map.get("clientFullName"));
		assertEquals(true, (boolean)map.get("isDebug"));
	}
	
	static String[] _list = {"[ {",
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
			
	public static List<Map<String, Object>> list = JsonHelper.parseList(String.join("", _list));
	
	@Test
	public void testParseList() {
		assertEquals("about", ((Map<String, Object>)TestJsonHelper.list.get(0)).get("id"));
	}
	
	@Test
	public void testFormatter() {
		String jsonStr = "{\"id\":\"1\",\"name\":\"a1\",\"obj\":{\"id\":11,\"name\":\"a11\",\"array\":[{\"id\":111,\"name\":\"a111\"},{\"id\":112,\"name\":\"a112\"}]}}";
		String fotmatStr = format(jsonStr);
		assertNotNull(fotmatStr);
	}

}