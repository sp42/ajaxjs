/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.web;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.Version;
import com.ajaxjs.framework.dao.Query;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.ConnectionMgr;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 封装常见的控制器方法。注意：不能复用 create/update 方法，这是因为 T 泛型不能正确识别 Bean 类型的缘故
 * 
 * @author Sp42 frank@ajaxjs.com
 * @param <T>
 *            实体类
 * @param <ID>
 *            实体 ID 字段类型
 */
public abstract class CommonController<T, ID extends Serializable> implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(CommonController.class);

	/**
	 * 对应的业务类
	 */
	private IService<T, ID> service;

	/**
	 * 是否输出 json 格式 Will it output data in JSON format?
	 */
	private boolean JSON_output;

	/**
	 * is in the ADMIN mode?
	 */
	private boolean adminUI;

	/**
	 * 初始化数据库连接
	 */
	public static void initDb() {
		String connStr = Version.isDebug ? "jdbc/sqlite" : "jdbc/sqlite_deploy";

		try {
			if (ConnectionMgr.getConnection() == null || ConnectionMgr.getConnection().isClosed()) {
				Connection conn = JdbcConnection.getConnection(JdbcConnection.getDataSource(connStr));
				ConnectionMgr.setConnection(conn);
				LOGGER.info("启动数据库链接……" + conn);
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 关闭数据库连接
	 */
	public static void closeDb() {
		try {
			ConnectionMgr.getConnection().close();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param start
	 *            起始行数，默认从零开始
	 * @param limit
	 *            偏量值，默认 8 笔记录
	 * @param model
	 *            Model 模型
	 * @return JSP 路径。缺省提供一个默认路径，但不一定要使用它，换别的也可以。
	 */
	public String list(int start, int limit, ModelAndView model) {
		LOGGER.info("获取列表 GET list:{0}/{1}", start, limit);

		initDb();

		IService<T, ID> service = getService(); // 避免 service 为单例

		PageResult<T> pageResult = null;
		try {
			pageResult = getService().findPagedList(getParam(start, limit));
			model.put("PageResult", pageResult);
		} catch (Exception e) {
			LOGGER.warning(e);
			model.put(errMsg, e);
		} finally {
			closeDb();
		}

		service.prepareData(model);

		if (pageResult == null)
			throw new NullPointerException("返回 null，请检查 service.findPagedList 是否给出实现");

		if (isJSON_output() && pageResult.getRows() != null && pageResult.getRows().get(0) instanceof Map) {// Map  类型的输出
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> l = (List<Map<String, Object>>) pageResult.getRows();
			model.put("MapOutput", JsonHelper.stringifyListMap(l));
		}

		if (isAdminUI())
			return String.format(jsp_adminList, service.getName());
		else
			return isJSON_output() ? paged_json_List : String.format(jsp_list, service.getName());
	}

	public QueryParams getParam(int start, int limit) {
		HttpServletRequest request = MvcRequest.getHttpServletRequest();
		QueryParams param = new QueryParams(start, limit);

		if (Query.isAnyMatch(request.getParameterMap())) // 其他丰富的查询参数
			param.query = Query.getQueryFactory(request.getParameterMap());

		return param;
	}

	/**
	 * 读取单个记录或者编辑某个记录，保存到 ModelAndView 中（供视图渲染用）。
	 * 
	 * @param id
	 *            ID 序号
	 * @param model
	 *            Model 模型
	 * @return JSP 路径。缺省提供一个默认路径，但不一定要使用它，换别的也可以。
	 */
	public void info(ID id, ModelAndView model) {
		LOGGER.info("读取单个记录或者编辑某个记录：id 是 {0}", id);

		initDb();
		IService<T, ID> service = getService(); // 避免 service 为单例

		T entry;
		try {
			entry = service.findById(id);
			model.put("info", entry);
		} catch (ServiceException e) {
			model.put(errMsg, e);
		}

		// model.put("neighbor", EntityUtil.getNeighbor(service.getName(),
		// id));// 相邻记录

		service.prepareData(model);

//		if (isAdminUI())
//			return String.format(jsp_adminInfo, service.getName());
//		else
//			return isJSON_output() ? show_json_info : String.format(jsp_info, service.getName());
	}

	public String list_all(ModelAndView model) {
		LOGGER.info("----获取全部列表----");
		return list(0, 999, model);
	}

	/**
	 * 保存到 request
	 * 
	 * @param request
	 *            请求对象
	 */
	public void saveToReuqest(ModelAndView mv, HttpServletRequest request) {
		for (String key : mv.keySet())
			request.setAttribute(key, mv.get(key));
	}

	/*
	 * 输出文档 GET /document
	 * 
	 * @param model Model 模型
	 * 
	 * @param entity POJO
	 * 
	 * @return
	 */
	// public String getDocument(ModelAndView model, T entity) {
	// String[] strs = DocumentRenderer.getEntityInfo(entity.getClass());
	// model.put("entityInfo", strs[0]);
	// if (strs[1] != null) { // 更多关于该实体的文档
	// model.put("moreDocument", strs[1]);
	// }
	//
	// model.put("meta", DocumentRenderer.getDocument(entity.getClass(),
	// service.getSQL_TableName()));
	//
	// return "common/entity/showDocument";
	// }

	public String createUI(ModelAndView model) {
		LOGGER.info("新建记录UI");

		initDb();
		IService<T, ID> service = getService();
		service.prepareData(model);

		model.put("isCreate", true);/*
									 * 因为新建/编辑（update）为同一套 jsp 模版，所以用 isCreate =
									 * true 标识为创建，以便与 update 区分开来。
									 */
		return String.format(jsp_adminInfo, service.getName());
	}

	// @POST
	// @Override
	public String create(T entity, ModelAndView model) {
		LOGGER.info("修改 name:{0}，数据库将执行 INSERT 操作", entity);
		initDb();

		try {
			ID newlyId = getService().create(entity);
			if (newlyId == null) {
				throw new ServiceException("创建失败！");
			}
			model.put("newlyId", newlyId);
		} catch (ServiceException e) {
			model.put(errMsg, e);
		} finally {
			closeDb();
		}

		return cud;
	}

	// @PUT
	// @Path("/{id}")
	/**
	 * 
	 * @param entity
	 * @param model
	 * @return JSP 路径，应返回 JSON 格式的
	 */
	public String update(/* @Valid */T entity, ModelAndView model) {
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", entity);
		initDb();
		model.put("isUpdate", true);

		try {
			getService().update(entity);
		} catch (ServiceException e) {
			model.put(errMsg, e);
		} finally {
			closeDb();
		}

		return cud;
	}

	public String delete(T entry, ModelAndView model) {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", entry);

		initDb();

		try {
			if (!getService().delete(entry)) {
				throw new ServiceException("删除失败！");
			}
		} catch (ServiceException e) {
			model.put(errMsg, e);
		} finally {
			closeDb();
		}

		return common_jsp_perfix + "delete.jsp";
	}
	
	//	public String delete(ID id, ModelAndView model) {
	//		
	//	}

	/**
	 * 显示 HTTP 405 禁止操作
	 * 
	 * @return HTTP 405 禁止操作 的 JSON
	 */
	public static String show405() {
		return String.format(json_not_ok, "405， Request method not supported 禁止操作");
	}

	public IService<T, ID> getService() {
		if (service == null)
			throw new NullPointerException("没有业务层对象！");
		return service;
	}

	public void setService(IService<T, ID> service) {
		this.service = service;
	}

	public boolean isJSON_output() {
		return JSON_output;
	}

	public void setJSON_output(boolean jSON_output) {
		JSON_output = jSON_output;
	}

	public boolean isAdminUI() {
		return adminUI;
	}

	public void setAdminUI(boolean adminUI) {
		this.adminUI = adminUI;
	}
}
