/* Copyright 2013-2015 www.snakerflow.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.workflow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.model.ProcessModel;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.util.CommonUtil;

/**
 * 流程定义
 * 
 * @author yuqs
 * @since 0.1
 */

@Path("/snaker/process")
public class ProcessController {

	private SnakerEngineFacets facets;

	/**
	 * 流程定义查询列表
	 * 
	 * @param model
	 * @return
	 */
	@GET
	@Path("list")
	public String processList(ModelAndView model, Page<Process> page, String displayName) {
		QueryFilter filter = new QueryFilter();
		if (!CommonUtil.isEmptyString(displayName))
			filter.setDisplayName(displayName);

		facets.getEngine().process().getProcesss(page, filter);
		model.put("page", page);
		return "snaker/processList";
	}

	/**
	 * 初始化流程定义
	 * 
	 * @return
	 */
	@GET
	@Path("init")
	public String processInit() {
		facets.initFlows();
		return "redirect:/snaker/process/list";
	}

	/**
	 * 根据流程定义部署
	 * 
	 * @param model
	 * @return
	 */
	@GET
	@Path("deploy")
	public String processDeploy(ModelAndView model) {
		return "snaker/processDeploy";
	}

	/**
	 * 新建流程定义[web流程设计器]
	 * 
	 * @param model
	 * @return
	 */
	@GET
	@Path("designer")
	public String processDesigner(String processId, ModelAndView model) {
		if (!CommonUtil.isEmptyString(processId)) {
			Process process = facets.getEngine().process().getProcessById(processId);

			ProcessModel processModel = process.getModel();
			if (processModel != null) {
				String json = SnakerHelper.getModelJson(processModel);
				model.put("process", json);
			}
			model.put("processId", processId);
		}
		return "snaker/processDesigner";
	}

	/**
	 * 编辑流程定义
	 * 
	 * @param model
	 * @return
	 */
	@GET
	@Path("edit/{id}")
	public String processEdit(ModelAndView model, @PathParam("id") String id) {
		Process process = facets.getEngine().process().getProcessById(id);
		model.put("process", process);

		if (process.getDBContent() != null) {
			try {
				model.put("content", StringHelper.textXML(new String(process.getDBContent(), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "snaker/processEdit";
	}

	/**
	 * 根据流程定义ID，删除流程定义
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("delete/{id}")
	public String processDelete(@PathParam("id") String id) {
		facets.getEngine().process().undeploy(id);
		return "redirect:/snaker/process/list";
	}

	/**
	 * 添加流程定义后的部署
	 * 
	 * @param snakerFile
	 * @param id
	 * @return
	 */
	@POST
	@Path("deploy")
	public String processDeploy(@RequestParam(value = "snakerFile") MultipartFile snakerFile, String id) {
		InputStream input = null;
		try {
			input = snakerFile.getInputStream();
			if (!CommonUtil.isEmptyString(id)) {
				facets.getEngine().process().redeploy(id, input);
			} else {
				facets.getEngine().process().deploy(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "redirect:/snaker/process/list";
	}

	/**
	 * 保存流程定义[web流程设计器]
	 * 
	 * @param model
	 * @return
	 */
	@POST
	@Path("deployXml")
	public boolean processDeploy(String model, String id) {
		InputStream input = null;
		try {
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
					+ SnakerHelper.convertXml(model);
			System.out.println("model xml=\n" + xml);

			input = StreamHelper.getStreamFromString(xml);
			if (!CommonUtil.isEmptyString(id)) {
				facets.getEngine().process().redeploy(id, input);
			} else {
				facets.getEngine().process().deploy(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	@GET
	@Path("json")
	@Produces(MediaType.APPLICATION_JSON)
	public Object json(String processId, String orderId) {
		List<Task> tasks = null;
		if (!CommonUtil.isEmptyString(orderId))
			tasks = facets.getEngine().query().getActiveTasks(new QueryFilter().setOrderId(orderId));

		Process process = facets.getEngine().process().getProcessById(processId);
		ProcessModel model = process.getModel();
		Map<String, String> jsonMap = new HashMap<>();

		if (model != null)
			jsonMap.put("process", SnakerHelper.getModelJson(model));

		// {"activeRects":{"rects":[{"paths":[],"name":"任务3"},{"paths":[],"name":"任务4"},{"paths":[],"name":"任务2"}]},"historyRects":{"rects":[{"paths":["TO
		// 任务1"],"name":"开始"},{"paths":["TO 分支"],"name":"任务1"},{"paths":["TO 任务3","TO
		// 任务4","TO 任务2"],"name":"分支"}]}}
		if (tasks != null && !tasks.isEmpty())
			jsonMap.put("active", SnakerHelper.getActiveJson(tasks));

		return jsonMap;
	}

	@GET
	@Path("display")
	public String display(ModelAndView model, String processId, String orderId) {
		HistoryOrder order = facets.getEngine().query().getHistOrder(orderId);
		model.put("processId", processId);
		model.put("order", order);

		List<HistoryTask> tasks = facets.getEngine().query().getHistoryTasks(new QueryFilter().setOrderId(orderId));
		model.put("tasks", tasks);

		return "snaker/processView";
	}

	/**
	 * 显示独立的流程图
	 */
	@GET
	@Path("diagram")
	public String diagram(ModelAndView model, String processId, String orderId) {
		model.put("processId", processId);
		model.put("orderId", orderId);
		return "snaker/diagram";
	}
}
