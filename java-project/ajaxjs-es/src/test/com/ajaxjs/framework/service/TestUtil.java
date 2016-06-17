package test.com.ajaxjs.framework.service;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.junit.Test;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.service.EsUtil;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.json.Json;
import com.ajaxjs.util.Util;
import com.egdtv.crawler.model.Album;
import com.egdtv.crawler.model.Video;
import com.egdtv.crawler.model.mapping.Dummy;

public class TestUtil {

	// @Test
	public void testCreate() {
		Client client = EsUtil.connectES();
		Album album = new Album();
		album.setName("foo");
		String newlyId = EsUtil.create(client, "crawler", "album", album);
		System.out.println(newlyId);
		assertNotNull(newlyId);
		client.close();
	}

//	 @Test
	public void testCreateIndex() {
		Client client = EsUtil.connectES();
		assertTrue(EsUtil.createIndex(client, "crawler"));
		client.close();
	}

//	@Test
	public void testDeleteIndex() {
		Client client = EsUtil.connectES();
		assertTrue(EsUtil.deleteIndex(client, "crawler"));
		client.close();
	}

	// 创建mapping 即是创 type，创建type 之前应该创建 index
//	 @Test
	public void testCreateMapping() {
		Client client = EsUtil.connectES();
		boolean isOk = EsUtil.createMapping(client, "crawler", "video", Util.getClassFolder_FilePath(Dummy.class, "Video.json"));
		assertTrue(isOk);
		client.close();
	}

	private static final String connectString = "jdbc:mysql://192.168.6.96:3306/topic_test?user=userttyy&password=2010@3gtv&useUnicode=true&characterEncoding=UTF-8";

	@Test
	public void testBulkInsert() {
		try (Connection connection = Helper.getConnection(connectString); ) {
			
			connection.prepareStatement("SET NAMES 'utf8mb4'").execute();

			List<Map<String, Object>> list = Helper.queryList(connection, "SELECT * FROM video LIMIT 25100, 10000");
			List<Video> videos = new Map2Pojo<>(Video.class).map2pojo(list);
			bulkInsertModel(videos, "crawler", "video");
	
//			System.out.println(list.get(0).get("tags"));
//			System.out.println(videos.get(0).getTags());
			System.out.println("批量创建索引成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private boolean bulkInsertModel(List<? extends BaseModel> list, String indexName, String typeName) {
		try(Client client = EsUtil.connectES();){
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			
			for (BaseModel pojo : list) {
				String json = Json.singlePojo(pojo);
				IndexRequest request = client.prepareIndex(indexName, typeName, pojo.getId() + "").setSource(json).request();
				bulkRequest.add(request);
			}
			
			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if (bulkResponse.hasFailures()) {
				System.err.println("批量创建索引错误！");
				return false;
			}
			return true;
		}
	}
}
