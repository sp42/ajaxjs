package com.ajaxjs.zinc.api;

import java.io.Serializable;
import java.util.Map;

import com.ajaxjs.net.http.Post;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.zinc.model.ZincResponse;

/**
 * 
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class DocumentService extends BaseService {
	public ZincResponse create(String target, Object bean) {
		return create(target, MapTool.bean2Map(bean), null);
	}

	public ZincResponse create(String target, Map<String, Object> doc) {
		return create(target, doc, null);
	}

	public ZincResponse create(String target, Object bean, Serializable id) {
		return create(target, MapTool.bean2Map(bean), id);
	}

	public ZincResponse create(String target, Map<String, Object> doc, Serializable id) {
		String url = apiWithId(target, id);
		Map<String, Object> result = Post.putJsonBody(url, doc, SET_HEAD);

		return result(result);
	}

	public ZincResponse update(String target, Object bean, Serializable id) {
		return update(target, MapTool.bean2Map(bean), id);
	}

	public ZincResponse update(String target, Map<String, Object> doc, Serializable id) {
		String url = apiWithId(target, id);
		Map<String, Object> result = Post.apiJsonBody(url, doc, SET_HEAD);

		return result(result);
	}
}
