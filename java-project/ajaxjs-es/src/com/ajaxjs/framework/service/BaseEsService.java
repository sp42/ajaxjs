package com.ajaxjs.framework.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.ui.Model;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.model.Query;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.Reflect;
import com.ajaxjs.util.map.MapHelper;

public class BaseEsService<T extends BaseModel> implements IService<T> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseService.class);

	private Class<T> reference;

	public Class<T> getReference() {
		return reference;
	}

	public void setReference(Class<T> reference) {
		this.reference = reference;
	}

	/**
	 * 连接本地 es 数据，返回 Client 对象。
	 * 
	 * @return
	 */
	private static Client connectES() {
		return connectES(null);
	}

	/**
	 * 连接本地 es 数据，返回 Client 对象。
	 * 
	 * @param clusterName
	 *            要连接的集群名称
	 * @return
	 */
	private static Client connectES(String clusterName) {
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
	 * 库名
	 */
	private String index;

	@Override
	public T getById(long id) throws ServiceException {
		checkPOJO_Class();

		T pojo = Reflect.newInstance(reference);
	
		GetResponse response = connectES().prepareGet(getIndex(), getTableName(), Long.toString(id)).getUUID();
		
		Map<String, Object> map = parseDate(response.getSourceAsMap());
		MapHelper.setMapValueToPojo(map, pojo);

		return pojo;
	}

	/**
	 * es 的日期有点奇怪。这里转换一下吧。
	 * @param map
	 * @return
	 */
	private static Map<String, Object> parseDate(Map<String, Object> map) {
		String timestamp_createDate = ((String)map.get("createDate")).replace("Z", " UTC"), 
			   timestamp_updateDate = ((String)map.get("updateDate")).replace("Z", " UTC");
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
		
		try {
			map.put("createDate", f.parse(timestamp_createDate));
			map.put("updateDate", f.parse(timestamp_updateDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return map;
	}

	private void checkPOJO_Class() {
		if (reference == null)
			throw new NullPointerException("这是 es 服务层，因为 java 泛型不能实例化，所以你必须得传个 class 进来，好让我能够实例化");
	}

	@Override
	public PageResult<T> getPageRows(int start, int limit, Query query) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int create(T entry) throws ServiceException {
		Map<String, Object> map = MapHelper.setPojoToMapValue(entry);
		

		IndexRequest request = connectES().prepareIndex("dept", "test", map.get("id").toString()).setSource(map).getUUID();
		return 0;
	}

	@Override
	public boolean update(T entry) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(T entry) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteByID(long id) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	private String tableName;

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModel(Model model) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getMappingTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getHidden_db_field_mapping() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSQL_TableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
