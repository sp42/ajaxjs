package com.ajaxjs.zinc;

import java.io.Serializable;
import java.util.Map;

import com.ajaxjs.net.http.Delete;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.zinc.model.ZincResponse;

/**
 * 实体（Document）的 CRUD
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class DocumentService extends BaseService {
	/**
	 * 创建实体
	 * 
	 * @param target index 名称
	 * @param bean   Java 实体 Bean
	 * @return
	 */
	public ZincResponse create(String target, Object bean) {
		return create(target, MapTool.bean2Map(bean), null);
	}

	/**
	 * 创建实体
	 * 
	 * @param target index 名称
	 * @param doc    Java 实体 Map
	 * @return
	 */
	public ZincResponse create(String target, Map<String, Object> doc) {
		return create(target, doc, null);
	}

	/**
	 * 创建实体
	 * 
	 * @param target index 名称
	 * @param bean   Java 实体 Bean
	 * @param id     实体 id
	 * @return
	 */
	public ZincResponse create(String target, Object bean, Serializable id) {
		return create(target, MapTool.bean2Map(bean), id);
	}

	/**
	 * 创建实体
	 * 
	 * @param target index 名称
	 * @param bean   Java 实体 Map
	 * @param id     实体 id
	 * @return
	 */
	public ZincResponse create(String target, Map<String, Object> doc, Serializable id) {
		String url = apiWithId(target, id);
		Map<String, Object> result = Post.apiJsonBody(url, doc, SET_HEAD);

		return result(result);
	}

	public ZincResponse update(String target, Object bean, Serializable id) {
		return update(target, MapTool.bean2Map(bean), id);
	}

	public ZincResponse update(String target, Map<String, Object> doc, Serializable id) {
		String url = apiWithId(target, id);
		Map<String, Object> result = Post.putJsonBody(url, doc, SET_HEAD);

		return result(result);
	}

	public ZincResponse delete(String target, Serializable id) {
		String url = apiWithId(target, id);
		Map<String, Object> result = Delete.api(url, SET_HEAD);

		return result(result);
	}
}
