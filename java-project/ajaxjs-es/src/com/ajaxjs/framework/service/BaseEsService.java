package com.ajaxjs.framework.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetRequestBuilder;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.ui.Model;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.Map2Pojo;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.model.Query;
import com.ajaxjs.util.Reflect;
import com.ajaxjs.util.MapHelper;

public class BaseEsService<T extends BaseModel> implements IService<T> {
	/**
	 * 索引名称
	 */
	private String indexName;

	/**
	 * 类型名称
	 */
	private String typeName;
	
	/**
	 * UI 显示的文字
	 */
	private String uiName; 		
	
	private Class<T> reference;

	public Class<T> getReference() {
		return reference;
	}

	public void setReference(Class<T> reference) {
		this.reference = reference;
	}

	@Override
	public T getById(long id) throws ServiceException {
		checkPOJO_Class();

		T pojo = Reflect.newInstance(reference);
	
		GetRequestBuilder response = EsUtil.connectES().prepareGet(getIndexName(), getTypeName(), Long.toString(id));
		
		System.out.println(response.get().getSourceAsMap());
		Map<String, Object> map = parseDate(response.get().getSourceAsMap());
		MapHelper.setMapValueToPojo(map, pojo);
	
		System.out.println(map);
		System.out.println(pojo.getName());
		

		return pojo;
	}

	/**
	 * es 的日期有点奇怪。这里转换一下吧。
	 * @param map
	 * @return
	 */
	private static Map<String, Object> parseDate(Map<String, Object> map) {
		if(map.get("createDate") == null || map.get("updateDate") == null)return map;
		
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
		SearchRequestBuilder searchRequestBuilder = EsUtil.connectES().prepareSearch(getIndexName());  
		searchRequestBuilder.setTypes(getTypeName()); 
		//设置查询类型  
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);  
		//设置分页信息  
		searchRequestBuilder.setFrom(start).setSize(limit);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		
		SearchHits searchHits = response.getHits();  // 总数
		SearchHit[] hits = searchHits.getHits();  

		List<Map<String, Object>> list = new ArrayList<>();
		
		for (SearchHit hit : hits) { 
			list.add(hit.getSource());
		}  
		
		List<T> videos = new Map2Pojo<>(getReference()).map2pojo(list);
		PageResult<T> result =new PageResult<>();
		result.setStart(start);
		result.setPageSize(limit);
		result.setTotalCount((int)searchHits.getTotalHits());
		result.setRows(videos);
		
		return result;
	}

	@Override
	public int create(T entry) throws ServiceException {
		Map<String, Object> map = MapHelper.setPojoToMapValue(entry);
		
		IndexRequestBuilder request = EsUtil.connectES().prepareIndex("dept", "test", map.get("id").toString()).setSource(map);
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
		return tableName;
	}

	@Override
	public String getUiName() {
		return uiName;
	}
	public void setUiName(String uiName){
		this.uiName = uiName;
	}

	@Override
	public Model getModel() {
		return null;
	}

	@Override
	public void setModel(Model model) {
	}

	@Override
	public String getMappingTableName() {
		return null;
	}

	@Override
	public Map<String, String> getHidden_db_field_mapping() {
		return null;
	}

	@Override
	public String getSQL_TableName() {
		return null;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return {@link #indexName}
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * @param indexName {@link #indexName}
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	/**
	 * @return {@link #typeName}
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName {@link #typeName}
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
