package test.com.ajaxjs.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapMock {
	public static Map<String, Object> user = new HashMap<String, Object>() {
		private static final long serialVersionUID = 1L;
		{
			put("id", 1L);
			put("name", "Jack");
			put("age", 30);
			put("birthday", new Date());
			
			put("children", "Tom,Peter");
			put("luckyNumbers", "2, 8, 6");
		}
	};
}
