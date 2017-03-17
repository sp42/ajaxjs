/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.net.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.map.MapHelper;

/**
 * 请求远程的 JSON 接口基类
 * 
 * @author frank
 *
 */
public abstract class RemoteJsonData implements RemoteData {
	private static final LogHelper LOGGER = LogHelper.getLog(RemoteJsonData.class);
	
	private final static ScriptEngine jsEngine = JsonHelper.engineFactory();

	/**
	 * 读取本地接口，把返回的 JSON 转换为 Map
	 * 
	 * @param request
	 *            请求对象
	 * @param url
	 *            URL 地址
	 * @return Map 列表
	 */
	public Map<String, Object>[] getLocalJSON_Array(HttpServletRequest request, String url) {
		return RemoteJsonData.getRemoteJSON_Array(url);
	}

	/**
	 * 默认为 /service 目录
	 * 
	 * @param request
	 *            请求对象
	 */
	public String getApiBaseUrl(HttpServletRequest request) {
		return "/service";
	}

	/**
	 * 讀取如下般結構的接口，爲 JSON 數組 [ { id: 3053, sectionName: "图集", sectionType: 1,
	 * jsonPrameters: "", secondQuery: "", description: "", parentId: 505,
	 * portalId: 5, createTime: "2014-07-07 16:14:28" }, { id: 529, sectionName:
	 * "免费", sectionType: 1, secondQuery: "", sortNo: 1, description: "免费频道",
	 * parentId: 505, portalId: 5, createTime: "2013-03-04 14:10:35" }, ... ]
	 * 
	 * http://u1.3gtv.net:2080/pms-service/section/subsection_list?id=224&start=
	 * 0&limit=99&contentType=4&sort=0
	 * 
	 * @param url
	 *            接口地址
	 * @param params
	 *            请求的参数
	 * @return 记录集合
	 */
	public Map<String, Object>[] getFlatList(String url, Map<String, String> params) {
		return RemoteJsonData.getRemoteJSON_Array(getApiUrl(url, params));
	}

	/**
	 * 读取远程接口，把返回的 JSON 转换为 Map[]
	 * 
	 * @param url
	 *            远程接口地址
	 * @return 响应内容 Map[]
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object>[] getRemoteJSON_Array(String url) {
		String json = Client.GET(url);

		if (!StringUtil.isEmptyString(json)) {
			List<Map<String, Object>> list = new JsonHelper(jsEngine).setJsonString(json).getList(null);
			
			Map<String, Object>[] maps = new HashMap[list.size()];
			
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> map =  MapHelper.toRealMap(list.get(i));
				
				if(map.get("id") != null && map.get("id") instanceof Double) { // id 自动类型转换 double2int
					map.put("id", JsonHelper.double2int((Double)map.get("id")));
				}
				
				maps[i] = map;
			}
			
			return maps;
//			return list.toArray(new Map[list.size()]);
		} else {
			LOGGER.warning("异常：读取远程接口，不能把返回的 JSON 转换为 Map[]！");
			return null;
		}
	}

	/**
	 * 读取远程接口，把返回的 JSON 转换为 Map
	 * 
	 * @param url
	 *            远程接口地址
	 * @return 响应内容 Map
	 */
	public static Map<String, Object> getRemoteJSON_Object(String url) {
		String json = Client.GET(url);

		if (!StringUtil.isEmptyString(json)) {
			// LOGGER.info(json);
			return new JsonHelper(jsEngine).setJsonString(json).getMap(null);
		} else {
			LOGGER.warning("异常：读取远程接口，不能把返回的 JSON 转换为 Map！");
			return null;
		}
	}

	@Override
	public String getApiUrl(String url, Map<String, String> params) {
		String apiUrl = getApiBaseUrl() + url + "?";

		if (getBaseParams() != null && getBaseParams() != null)
			apiUrl += StringUtil.HashJoin(getBaseParams(), '&');

		if (params != null) {
			if (apiUrl.endsWith("?"))
				apiUrl += StringUtil.HashJoin(params, '&');
			else
				apiUrl += "&" + StringUtil.HashJoin(params, '&');
		}

		return apiUrl;
	}

	@Override
	public Map<String, Object> getInfo(String url, Map<String, String> params) {
		String _url = getApiUrl(url, params); // 注入！
		return RemoteJsonData.getRemoteJSON_Object(_url);
	}

	@Override
	public Map<String, Object> getInfoById(String url, int id) {
		return getInfoById(url, id + "");
	}

	@Override
	public Map<String, Object> getInfoById(String url, String id) {
		Map<String, String> params = new HashMap<>();
		params.put(getId_field(), id);
		Map<String, Object> map = getInfo(url, params);
		return map;
	}

	@Override
	public ListResult getList(String url, Map<String, String> params) {
		ListResult result = new ListResult(); // 不要这个对象 null，让 result.results 可以
												// null
		Map<String, Object> response = RemoteJsonData.getRemoteJSON_Object(getApiUrl(url, params));

		if (response != null && response.get(getTotalToken()) != null) {
			result.total = JsonHelper.double2int((Double) response.get(getTotalToken()));

			if (result.total <= 0) {
				LOGGER.warning("存在总数字段，但为 0，查询结果为零，返回 null");
			} else if (response.get(getResultToken()) == null) {
				LOGGER.warning("记录记录数：{0}。但没有数据列表返回！！！", result.total);
			} else {
//				result.results = Rhino.NativeArray2MapArray((NativeArray)response.get(getResultToken()));
			}
		} else {
			if (response == null)
				LOGGER.warning("没有返回 JSON");
			else
				LOGGER.warning("不存在 {0} 总数字段，返回 null", getTotalToken());
		}

		return result;
	}

	@Override
	public String getId_field() {
		return "id";
	}

	@Override
	public String getTotalToken() {
		return "total";
	}

	@Override
	public String getResultToken() {
		return "result";
	}
}
