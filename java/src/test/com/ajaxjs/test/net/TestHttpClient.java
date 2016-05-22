package com.ajaxjs.test.net;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.net.http.HttpClient;
import com.ajaxjs.net.http.Post;


public class TestHttpClient {

	@Test
	public void testGet() {
		String url = "https://baidu.com";
		String html = HttpClient.GET(url);
		System.out.println(html);
		assertNotNull(html);

		html = HttpClient.simpleGET(url);
		System.out.println(html);
		assertNotNull(html);
	}

	@Test
	public void testJSON() {
		String url = "http://ip.taobao.com/service/getIpInfo.php?ip=63.223.108.42";
		Map<String, Object> map = HttpClient.getRemoteJSON_Object(url);

		System.out.println(map);
		assertNotNull(map);
	}

	@Test
	public void testGet302redirect() {
		String url = "https://baidu.com";
		try {
			String location = HttpClient.get302redirect(url);
			System.out.println(location);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPost() {
		String url = "http://42.159.147.4:8080/topic/audioinfo/saves.to";
		Post post = new Post(url);
		post.setRequestData("audioList={albumName:\"213213\"," + "audioContent:[ "
				+ "{ content: \"朝鲜氢弹试验不成功又怎样？朝鲜氢弹试验不成功又怎样？\", 	 sourceUrl: 	 \"http://od.qingting.fm/m4a/5692efe57b28aa1a69426337_4663983_64.m4a\", 	 createDate: \"2016-01-20 21:32:12\", updateDate: \"2016-01-20 21:32:12\", name: 	 \"朝鲜氢弹试验不成功又怎样？\" }, "
				+ "{ content: \"朝鲜氢弹试验不成功又怎样？朝鲜氢弹试验不成功又怎样？\", 	 sourceUrl: 	 \"http://od.qingting.fm/m4a/5692efe57b28aa1a69426337_4663983_64.m4a\", 	 createDate: \"2016-01-20 21:32:12\", updateDate: \"2016-01-20 21:32:12\", name: 	 \"朝鲜氢弹试验不成功又怎样？\" } 	"
				+ "] 	" + "}");
		System.out.println(post.getText());
	}

	@Test
	public void testAlbum() {
		String url = "http://42.159.147.4:8080/topic/albuminfo/betchSave.to";
		Post post = new Post(url);
		String str = "albumList=";

		try {
			str += URLEncoder.encode(
					"[{\"albumname\":\"雨轩&洋洋《方言社会》&《洋芋摆巴适》\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201508/52e3c17b-73a9-4b22-a08f-7771d886c633/default.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000044737\",\"updateTime\":\"\"},{\"albumname\":\"你真的知道吗\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201512/ef52800e-3c4a-4642-89fb-6aee7e8e6a5c/default.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000000207\",\"updateTime\":\"\"},{\"albumname\":\"大咖访谈\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201507/5156cbb5-224d-42b5-a4ce-55426aaeecba/default.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000000710\",\"updateTime\":\"\"},{\"albumname\":\"地球怎么了\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201405/cc7e5baa-a593-4df3-8b58-a76a40c523fd/default.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000001820\",\"updateTime\":\"\"},{\"albumname\":\"经典传奇\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201409/0dfa92a4-5e99-405a-a6b4-5b8be097f390/100_100.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000000463\",\"updateTime\":\"\"}]",
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		post.setRequestData(str);
		post.setRequestData("name=hjj&aaa=564&564");
		System.out.println(post.getText());
	}

	@Test
	public void testName() {
		String url = "http://192.168.1.141:8080/pachong/service/video";
		Post post = new Post(url);
		String str = "name=2016-03-12%E4%B8%96%E7%BA%AA%E5%A4%A7%E8%AE%B2%E5%A0%82+%E4%BE%9B%E7%BB%99%E4%BE%A7%E6%94%B9%E9%9D%A9%E7%9A%84%E2%80%9C%E6%AD%BC%E7%81%AD%E6%88%98%E2%80%9D&sourceUrl=http%3A%2F%2Fips.ifeng.com%2Fwideo.ifeng.com%2Fdocumentary%2F2016%2F03%2F12%2F3892810-102-2014.mp4&ownerUrl=http%3A%2F%2Fv.ifeng.com%2Fgongkaike%2Fsjdjiangtang%2F201603%2F05910969-7858-41c8-9b8a-dc646022147f.shtml&cover=http%3A%2F%2Fd.ifengimg.com%2Fw400_h300%2Fy0.ifengimg.com%2Fpmop%2F2016%2F03%2F12%2Ff40bc0cf-4b9d-4e0a-bb1b-231d4c76ef1d.jpg&duration=36%3A03&publishDate=2016-03-12+20%3A12&portalId=8&albumId=&catalog=28";

		// try {
		// str+=
		// URLEncoder.encode("[{\"albumname\":\"雨轩&洋洋《方言社会》&《洋芋摆巴适》\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201508/52e3c17b-73a9-4b22-a08f-7771d886c633/default.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000044737\",\"updateTime\":\"\"},{\"albumname\":\"你真的知道吗\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201512/ef52800e-3c4a-4642-89fb-6aee7e8e6a5c/default.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000000207\",\"updateTime\":\"\"},{\"albumname\":\"大咖访谈\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201507/5156cbb5-224d-42b5-a4ce-55426aaeecba/default.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000000710\",\"updateTime\":\"\"},{\"albumname\":\"地球怎么了\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201405/cc7e5baa-a593-4df3-8b58-a76a40c523fd/default.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000001820\",\"updateTime\":\"\"},{\"albumname\":\"经典传奇\",\"category\":\"脱口秀\",\"contentnumber\":\"\",\"cover\":\"http://image.kaolafm.net/mz/images/201409/0dfa92a4-5e99-405a-a6b4-5b8be097f390/100_100.jpg\",\"creatTime\":\"\",\"description\":\"\",\"source\":\"考拉FM\",\"sourceurl\":\"http://m.kaolafm.com/share/zj.html?albumId=1100000000463\",\"updateTime\":\"\"}]",
		// "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		post.setRequestData(str);
		System.out.println(post.getText());
	}

}
