package test.com.ajaxjs.framework.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest; 
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
 

/**
 * 当前没有数据库连接
 * @author frank
 *
 */
public class TestJsonService extends MockRequest{
	@BeforeClass
	public static void initApplication() throws Exception{
		TestApplication app = new TestApplication();
		app.setUp();
		app.testContextInitialized();
	}
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testAllList() throws IOException, NamingException, ServletException {
		HttpServletRequest request = initRequest("news/");
		Map<String, Object>[] json = shouldbe_jsonArray_return(doRequest(request));
		
		assertNotNull(json);
		assertTrue(json.length > 0);
	}
	
	@Test
	public void testPagedList() throws IOException, NamingException, ServletException {
		HttpServletRequest request = initRequest("news/");
		when(request.getParameter("start")).thenReturn("0");
		when(request.getParameter("limit")).thenReturn("3");
		
		Map<String, Object> json = shouldbe_json_return(doRequest(request));
		
		assertNotNull(json);
		assertTrue(shouldbe_hasRecord(json));
	}

	@Test
	public void testNews() throws IOException, NamingException, ServletException {
		HttpServletRequest request = initRequest("news/49d09367-8678-4ecb-8921-d24320e11f96/");
		String jsonStr = doRequest(request);
		Map<String, Object> json = shouldbe_json_return(jsonStr);
		
		assertNotNull(json);
		assertNotNull(json.get("name"));
	}
}
