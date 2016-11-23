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
package com.ajaxjs.mvc.controller.common;

import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;

/**
 * 
 * 在 ReadOnlyController 基础上增加写操作
 * 不能复用 create 方法，这是因为 Bean 类型识别的缘故
 * @author frank
 *
 * @param <T>
 */
public abstract class Read_Create_Controller<T extends BaseModel> extends ReadOnlyController<T> {
	@PUT
	@Path("/{id}")
	@Override
	public String update(@PathParam("id") long id, T entity, ModelAndView model) {
		return super.update(id, entity, model);
	}
	
	@DELETE
	@Path("/{id}")
	@Override
	public String delete(@PathParam("id") long id, ModelAndView model) {
		return super.delete(id, model);
	}
}
