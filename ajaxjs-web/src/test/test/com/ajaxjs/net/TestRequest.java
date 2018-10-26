package test.com.ajaxjs.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.keyvalue.MappingValue;
import com.ajaxjs.web.mock.MockFilter;
import com.ajaxjs.web.mock.MockRequest;

public class TestRequest extends MockFilter {
	@BeforeClass
	public static void init() {
	}
	// http://ip.taobao.com/service/getIpInfo.php?ip=63.223.108.42
	@Before
	public void setUp() {
	}

	@Test
	public void testRequestHelper() throws IOException {
		Map<String, String> hash = new HashMap<>();
		hash.put("name", "bar");
		hash.put("id", "1001");
		hash.put("isGood", "true");

		hash.put("ChineseName", new String("张三".getBytes(), "ISO8859_1"));

		HttpServletRequest request = MockRequest.mockRequest("", "");
		request = MockRequest.mockFormRequest(request, hash, true);

		assertNotNull(request);

		assertEquals("bar", request.getParameter("name"));

		IllegalArgumentException knownException = null;

		try {
			request.getParameter("你好");
		} catch (IllegalArgumentException e) {
			knownException = e;
		} finally {
			// 总是有异常的
			assertNotNull("捕获已知异常", knownException);
		}

		assertEquals(1001, (int) MappingValue.toJavaValue(request.getParameter("id")));
		assertTrue((boolean) MappingValue.toJavaValue(request.getParameter("isGood")));

		assertEquals("张三", request.getParameter("ChineseName"));
	}
}
