package test.com.ajaxjs.net;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.*;

import com.ajaxjs.net.http.RemoteJsonData;

public class TestRemoteJsonData {
 
	@Test
	public void testJSON() {
		String url = "http://ip.taobao.com/service/getIpInfo.php?ip=63.223.108.42";
		Map<String, Object> map = RemoteJsonData.getRemoteJSON_Object(url);

		System.out.println(map);
		assertNotNull(map);
	}
}
