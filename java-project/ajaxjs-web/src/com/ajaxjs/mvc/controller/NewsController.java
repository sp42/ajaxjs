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
package com.ajaxjs.mvc.controller;

import javax.mvc.annotation.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.News;
import com.ajaxjs.framework.service.NewsService;


/**
 * 
 * @author frank
 *
 */
@Controller
@Path("/news")
public class NewsController extends AbstractCrudController<News> {
	public NewsController() {
		setService(new NewsService());
		setEnableDefaultWrite(false);
	}
	
	@GET
	public String redirect() {
		return "redirect:show_video/list";
	}
	
	@GET
	@Path("/list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit,  ModelAndView model) {

		super.list(start, limit, model);
//		Common.initFilters(request, "video");
		return "";
	}
	
	@GET
	@Path("/{id}")
	@Override
	public String getById(@PathParam("id") long id, ModelAndView model) {
		System.out.println("---------------------" + id);
		super.getById(id, model);
		
		return "home.jsp";
	}
	
//	@Override
//	public void prepareData(HttpServletRequest request, Model model) {
//		Common.prepareData(model);
//	}
//	
	 
 
}
