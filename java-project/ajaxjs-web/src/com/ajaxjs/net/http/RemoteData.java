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

import java.util.Map;

/**
 * 读取远程接口，可以是 JSON 接口/XML 接口或者 WebService。此部分又称 Data transfer object (DTO)
 * 
 * @author frank
 *
 */
public interface RemoteData {
	/**
	 * 获取 API URL 前缀 例如：http://localhost:8080/gz88/service
	 * 
	 * @return 接口地址前段部分
	 */
	String getApiBaseUrl();

	/**
	 * 返回必备的参数 Map
	 * 
	 * @return 每次请求都发出去的参数，用 map 定义
	 */
	Map<String, String> getBaseParams();

	/**
	 * 生成接口最终的 URL
	 * 
	 * @param url
	 *            不包含参数的 url 部分，是前缀
	 * @param params
	 *            请求的参数
	 * @return 最终接口地址
	 */
	String getApiUrl(String url, Map<String, String> params);

	/**
	 * 获取单个对象，JSON 结构如 {"foo":bar};
	 * 
	 * @param url
	 *            不包含前缀的 url 部分
	 * @param params
	 *            请求的参数
	 * @return 实体数据，用 map 结构
	 */
	Map<String, Object> getInfo(String url, Map<String, String> params);

	/**
	 * 根据 id:int 获取单个对象
	 * 
	 * @param url
	 *            不包含前缀的 url 部分
	 * @param id
	 * @return 实体数据，用 map 结构
	 */
	Map<String, Object> getInfoById(String url, int id);

	/**
	 * 根据 id:String 获取单个对象
	 * 
	 * @param url
	 *            不包含前缀的 url 部分
	 * @param id
	 * @return 实体数据，用 map 结构
	 */
	Map<String, Object> getInfoById(String url, String id);

	/**
	 * 获取列表数据，JSON 形如：{ total:33, results:[ { foo:bar, …… …… } ] }
	 * 
	 * @param url
	 *            不包含前缀的 url 部分
	 * @param params
	 *            请求的参数
	 * @return
	 */
	ListResult getList(String url, Map<String, String> params);

	/**
	 * 请求的 id 参数具体是什么，例如 id／uid／uuid
	 * 
	 * @return id 参数名称
	 */
	String getId_field();

	/**
	 * 获取 JSON 总数的字段名，例如 total／totalCounts
	 * 
	 * @return 总数的 key
	 */
	String getTotalToken();

	/**
	 * 获取结果集合 token 的字段名，例如 result
	 * 
	 * @return 记录集合的 key
	 */
	String getResultToken();

	/**
	 * 列表结果集，简单定义一下
	 * 
	 * @author frank
	 *
	 */
	public static class ListResult {
		public int total; // 总数
		public Map<String, Object>[] results;// 记录集合
	}
}
