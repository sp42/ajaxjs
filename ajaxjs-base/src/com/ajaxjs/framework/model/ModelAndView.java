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
package com.ajaxjs.framework.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.bval.jsr.ApacheValidationProvider;

public class ModelAndView extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	// BVal 与 JSR 接口结合，返回 ValidatorFactory 工厂
	private final static ValidatorFactory avf = Validation.byProvider(ApacheValidationProvider.class).configure().buildValidatorFactory();

	/**
	 * BVal 与 JSR 接口结合，返回 ValidatorFactory 工厂
	 * @return 验证器
	 */
	public static Validator getValidator() {
		return avf.getValidator();
	}
	

	/**
	 * 把列表转换为 map，以 id 作为键值。 key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param bean
	 *            实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Integer, BaseModel> list_bean2map_id_as_key(List<? extends BaseModel> bean) {
		if (bean == null || bean.size() == 0)
			return null;

		Map<Integer, BaseModel> map = new HashMap<>();

		for (BaseModel item : bean) {
			map.put(new Long(item.getId()).intValue(), item);
		}
		return map;
	}

	public static Map<Integer, Object> list2map_id_as_key(List<Map<String, Object>> list) {
		if (list == null)
			return null;
		Map<Integer, Object> map = new HashMap<>();

		for (Map<String, Object> item : list) {
			map.put(new Long(item.get("id").toString()).intValue(), item);
		}
		return map;
	}
}
