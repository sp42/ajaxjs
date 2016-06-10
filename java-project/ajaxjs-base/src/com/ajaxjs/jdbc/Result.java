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
package com.ajaxjs.jdbc;

import java.util.Date;
import java.util.Map;

/**
 * 查询结果 该类字段设计符合了充分方便 JSON 接口输出的原则（故内部也是用 Rhino 遍历 ResutSet，直接返回 JSON）。
 * 接口每次请求完毕，都会读取 Result.json 的结果。 如果要在 Java 中作业务逻辑处理，则读取 Result.result/results
 * 即可。
 * 
 * 
 * @author Frank Cheung
 */
public abstract class Result<T> {
	/**
	 * 操作是否成功了（通常对 UPDATE、INSERT、DELETE 有意义）
	 */
	public boolean isOk = false;

	/**
	 * 是否有记录
	 */
	public boolean isNotNull;

	public String json; // 返回给前端的 JSON 字符串
	public String sqlStatement; // 执行过查询的那个 SQL 语句。可用于追溯
	public Map<String, Object> result; // 已转换为 Map 的结果，如果复数则是 null
	public Map<String, Object>[] results;// 已转换为 Map 结果集合，如果单数则是 null
	public Date queryTime; // 查询耗时
	public int total; // 总记录数

	public T info;
	public T[] infoList;
}
