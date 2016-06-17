package com.ajaxjs.framework.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.ajaxjs.json.Json;
import com.ajaxjs.util.FileUtil;
import com.ajaxjs.framework.model.BaseModel;

public class EsUtil {

	/**
	 * 连接本地 es 数据，返回 Client 对象。
	 * 
	 * @return 连接客户端对象
	 */
	public static Client connectES() {
		return connectES(null);
	}

	/**
	 * 连接本地 es 数据，返回 Client 对象。
	 * 
	 * @param clusterName
	 *            要连接的集群名称
	 * @return 连接客户端对象
	 */
	public static Client connectES(String clusterName) {
		InetAddress address = null;
		try {
			address = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}

		if (clusterName == null)
			clusterName = "elasticsearch";

		Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
		Client client = TransportClient.builder().settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(address, 9300));

		return client;

	}

	/**
	 * 保存一个 Model 到 ES
	 * 
	 * @param client
	 *            连接客户端对象
	 * @param indexName
	 *            索引名称
	 * @param typeName
	 *            类型名称
	 * @param model
	 *            POJO/Bean/模型
	 * @return true 表示为创建记录成功
	 */
	public static String create(Client client, String indexName, String typeName, BaseModel model) {
		String json = Json.singlePojo(model);
		IndexResponse response = client.prepareIndex(indexName, typeName).setSource(json).get();

		// isCreated() is true if the document is a new one, false if it has
		// been updated
		boolean created = response.isCreated();
		if (created) {
			return response.getId();
		} else {
			System.out.println("已存在记录！");
			return null;
		}
	}
	
	/**
	 * 创建一个索引
	 * 
	 * @param client
	 *            连接客户端对象
	 * @param indexName
	 *            索引名称
	 * @return true 表示为创建索引成功
	 */
	public static boolean createIndex(Client client, String indexName){
		CreateIndexResponse response = client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet();

		return response.isAcknowledged();
	}

	/**
	 * 删除一个索引
	 * 
	 * @param client
	 *            连接客户端对象
	 * @param indexName
	 *            索引名称
	 * @return true 表示为删除索引成功
	 */
	public static boolean deleteIndex(Client client, String indexName) {
		DeleteIndexResponse delete = client.admin().indices().delete(Requests.deleteIndexRequest(indexName))
				.actionGet();
		return delete.isAcknowledged();
	}

	/**
	 * 创建映射
	 * 
	 * @param client
	 *            连接客户端对象
	 * @param indexName
	 *            索引名称
	 * @param typeName
	 *            类型名称
	 * @param filePath
	 *            这是一个 json 文件名，保存了映射
	 * @return true 表示为创建映射成功
	 */
	public static boolean createMapping(Client client, String indexName, String typeName, String filePath) {
		String mapping;
		try {
			mapping = FileUtil.readFileAsText(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		PutMappingRequest mappingRequest = Requests.putMappingRequest(indexName).type(typeName).source(mapping);
		PutMappingResponse resposne = client.admin().indices().putMapping(mappingRequest).actionGet();

		return resposne.isAcknowledged();
	}

	public static boolean deleteById(Client client, String indexName, String typeName, String id) {
		DeleteResponse response = client.prepareDelete(indexName, typeName, id).get();
		return response.getId() != null;
	}
	
	/**
	 * 批量创建索引
	 * 
	 * @param client
	 * @param list
	 * @return
	 */
	public static boolean bulkInsert(Client client, List<Map<String, Object>> list) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Map<String, Object> item : list) {
//			System.out.println(item.get("id").getClass().getName());
			IndexRequest request = client.prepareIndex("dept", "test", item.get("id").toString()).setSource(item).request();
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
