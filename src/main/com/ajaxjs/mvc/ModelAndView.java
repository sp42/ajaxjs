/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.util.collection.CollectionUtil;

/**
 * 数据实体转换到页面层的对象。携带的数据，可用于 UI 显示或者其他数据
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class ModelAndView extends HashMap<String, Object> {
	private static final long serialVersionUID = -3437249813346729635L;

	/**
	 * 把列表（BaseModel 结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param bean
	 *            实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Integer, BaseModel> list_bean2map_id_as_key(List<? extends BaseModel> bean) {
		if (CollectionUtil.isNull(bean))
			return null;

		Map<Integer, BaseModel> map = new HashMap<>();

		for (BaseModel item : bean)
			map.put(new Long(item.getId()).intValue(), item);

		return map;
	}

	/**
	 * 把列表（Map结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param list
	 *            实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Integer, Object> list2map_id_as_key(List<Map<String, Object>> list) {
		if (CollectionUtil.isNull(list))
			return null;

		Map<Integer, Object> map = new HashMap<>();

		for (Map<String, Object> item : list)
			map.put(new Long(item.get("id").toString()).intValue(), item);

		return map;
	}
}
