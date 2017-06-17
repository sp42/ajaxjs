package test.com.ajaxjs.util.reflect;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.reflect.BeanUtil;

import test.com.ajaxjs.mock.MapMock;
import test.com.ajaxjs.mock.User;

public class TestBeanUtil {
	public static Map<String, Object> userWithoutChild = new HashMap<String, Object>() {
		private static final long serialVersionUID = 1L;
		{
			put("id", 1L);
			put("name", "Jack");
			put("age", 30);
			put("birthday", new Date());
		}
	};

	@Test
	public void testMap2Bean() {
		User user = BeanUtil.map2Bean(userWithoutChild, User.class);// 直接转
		assertNotNull(user);
		assertEquals(user.getName(), "Jack");
		
		user = BeanUtil.map2Bean(MapMock.user, User.class, true);
		assertNotNull(user);
		assertEquals(user.getChildren()[0], "Tom");
		assertEquals(user.getLuckyNumbers()[1], 8);
		assertEquals(user.isSex(), true);
	}
}
