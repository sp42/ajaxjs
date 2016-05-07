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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.ui.Model;

import com.ajaxjs.web.Requester;

/**
 * 符合 Spring MVC model 接口的实现类，多用于前端 JSP 用，因为控制器都是 Spring MVC 写的要调用的话，必须符合一致的接口。
 * @author frank
 *
 */
public class JspModel extends HttpServletRequestWrapper implements Model {
	public JspModel(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Model addAllAttributes(Collection<?> arg0) {
		return null;
	}

	@Override
	public Model addAllAttributes(Map<String, ?> arg0) {
		return null;
	}

	@Override
	public Model addAttribute(Object arg0) {
		return null;
	}
	
	private Map<String, Object> map = new HashMap<>();
	
	@Override
	public Model addAttribute(String arg0, Object arg1) {
		setAttribute(arg0, arg1);
		map.put(arg0, arg1);
		return this;
	}

	@Override
	public Map<String, Object> asMap() {
		return map;
	}

	@Override
	public boolean containsAttribute(String arg0) {
		return false;
	}

	@Override
	public Model mergeAttributes(Map<String, ?> arg0) {
		return null;
	}

	public int getStart(){
		String start = getParameter("start");
		if(start == null)start = "0";
		
		return Integer.parseInt(start);
	}
	public int getLimit(){
		String limit = getParameter("limit");
		if(limit == null)limit = "8";
		
		return Integer.parseInt(limit);
	}

	/**
	 * 获取 id
	 * @return
	 */
	public int getId(){
		String id = new Requester(this).getParameter("id");
		return Integer.parseInt(id);
	}
	
	public Entity getBean() {
		Object obj = getAttribute("info");
		return (Entity) obj;
	}

	/**
	 * 把列表转换为 map，以 id 作为键值。 key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param bean
	 *            实体列表
	 * @return
	 */
	public static Map<Integer, BaseModel> list_bean2map_id_as_key(List<? extends BaseModel> bean) {
		Map<Integer, BaseModel> map = new HashMap<>();
		
		for (BaseModel item : bean) {
			map.put(new Long(item.getId()).intValue(), item);
		}
		return map;
	}
	
	public static Map<Integer, Object> list2map_id_as_key(List<Map<String, String>> list) {
		if(list == null) return null;
		Map<Integer, Object> map = new HashMap<>();
		
		for (Map<String, String> item : list) {
			map.put(new Long(item.get("id")).intValue(), item);
		}
		return map;
	}

}
