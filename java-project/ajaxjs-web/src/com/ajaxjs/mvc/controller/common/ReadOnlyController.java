package com.ajaxjs.mvc.controller.common;

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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.AbstractController;

/**
 * 只读输出，有 /{id} 和 /list 两个路由，
 * 可以选择 json/html 输出。
 * @author frank
 *
 * @param <T>
 */
public abstract class ReadOnlyController<T extends BaseModel> extends AbstractController<T> {
	/**
	 * 是否输出 json 格式
	 */
	private boolean JSON_output;
	
	@GET
	@Path("/list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		if(isJSON_output()) {
			super.list(start, limit, model);
			return pagedList;
		}else 
			return jsp_perfix + super.list(start, limit, model) + "/frontEnd_list";
	}
	
	@GET
	public String redirect() {
		return "redirect:show_video/list";
	}

	@GET
	@Path("/{id}")
	@Override
	public String getById(@PathParam("id") long id, ModelAndView model) {
		if(isJSON_output()) {
			super.getById(id, model);
			return showInfo;
		} else
			return jsp_perfix + super.getById(id, model) + "/frontEnd_info";
	}

	public boolean isJSON_output() {
		return JSON_output;
	}

	public void setJSON_output(boolean jSON_output) {
		JSON_output = jSON_output;
	}
}
