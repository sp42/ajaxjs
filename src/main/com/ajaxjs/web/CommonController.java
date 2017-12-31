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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.Version;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 封装常见的控制器方法。注意：不能复用 create/update 方法，这是因为 T 泛型不能正确识别 Bean 类型的缘故 需要有 Service 层
 * 
 * @author Sp42 frank@ajaxjs.com
 * @param <T>
 *            实体类型，可以是 Bean（POJO） 或 Map
 * @param <ID>
 *            ID 类型，可以是 INTEGER/LONG/String
 */
public abstract class CommonController<T, ID extends Serializable, S extends IService<T, ID>> implements IController, Constant {
	private static final LogHelper LOGGER = LogHelper.getLog(CommonController.class);

	/**
	 * 对应的业务类
	 */
	private S service;

	/**
	 * 初始化数据库连接
	 * 
	 * @param connStr
	 */
	public static void initDb(String connStr) {
		try {
			if (JdbcConnection.getConnection() == null || JdbcConnection.getConnection().isClosed()) {
				Connection conn = JdbcConnection.getConnection(JdbcConnection.getDataSource(connStr));
				JdbcConnection.setConnection(conn);
				LOGGER.info("启动数据库链接……" + conn);
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 初始化数据库连接
	 */
	public static void initDb() {
		//		connStr = Version.isDebug ? "jdbc/sqlite" : "jdbc/sqlite_deploy";
		initDb(Version.isMac ? "jdbc/sqlite_mac" : "jdbc/sqlite");
	}

	/**
	 * 关闭数据库连接
	 */
	public static void closeDb() {
		Connection conn = JdbcConnection.getConnection();

		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		JdbcConnection.clean();
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
	public PageResult<T> pageList(int start, int limit, ModelAndView model) {
		LOGGER.info("获取列表 GET list:{0}/{1}", start, limit);
		initDb();
		prepareData(model);

		IService<T, ID> service = getService(); // 避免 service 为单例

		PageResult<T> pageResult = null;
		try {
			pageResult = service.findPagedList(getParam(start, limit));
			model.put("PageResult", pageResult);
		} catch (Exception e) {
			LOGGER.warning(e);
			model.put(errMsg, e);
		} finally {
			closeDb();
		}

		if (model.get(errMsg) != null)
			LOGGER.warning("严重异常，请检查 service.findPagedList() 是否给出实现");

		return pageResult;
	}

	@SuppressWarnings("unchecked")
	public String outputPagedJsonList(PageResult<T> pageResult, ModelAndView model) {
		if (pageResult.getRows() != null) {
			String jsonStr;

			if (pageResult.getRows().get(0) instanceof Map) { // Map 类型的输出
				List<Map<String, Object>> list = (List<Map<String, Object>>) pageResult.getRows();
				jsonStr = JsonHelper.stringifyListMap(list);
			} else { // Bean
				jsonStr = JsonHelper.beans2json((List<Object>) pageResult.getRows());
			}

			model.put("MapOutput", jsonStr);
		}

		return paged_json_List;
	}

	public static QueryParams getParam(int start, int limit) {
		HttpServletRequest request = MvcRequest.getHttpServletRequest();
		QueryParams param = new QueryParams(start, limit, request.getParameterMap());

		return param;
	}

	/**
	 * 可覆盖的模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param model
	 *            模型
	 */
	public void prepareData(ModelAndView model) {
		// 每次 servlet 都会执行的。记录时间
		model.put("requestTimeRecorder", System.currentTimeMillis());

		if (service != null) {
			// 设置实体 id 和 现实名称 。
			model.put("uiName", service.getName());
			model.put("tableName", service.getTableName());
		}
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
	public String info(ID id, ModelAndView model) {
		LOGGER.info("读取单个记录或者编辑某个记录：id 是 {0}", id);
		initDb();
		prepareData(model);

		IService<T, ID> service = getService(); // 避免 service 为单例

		T entry;
		try {
			entry = service.findById(id);
			model.put("info", entry);
		} catch (ServiceException e) {
			model.put(errMsg, e);
		}

		return String.format(jsp_info, service.getTableName());
	}

	/**
	 * 获取全部列表数据
	 * 
	 * @param model
	 *            Model 模型
	 */
	public void list_all(ModelAndView model) {
		LOGGER.info("----获取全部列表----");
		pageList(0, 999, model);
	}

	/**
	 * 保存到 request
	 * 
	 * @param request
	 *            请求对象
	 */
	public static void saveToReuqest(ModelAndView mv, HttpServletRequest request) {
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

	/**
	 * 
	 * @param model
	 *            页面 Model 模型
	 * @return
	 */
	public String createUI(ModelAndView model) {
		LOGGER.info("新建记录UI");

		IService<T, ID> service = getService();
		prepareData(model);

		model.put("isCreate", true);/*
									 * 因为新建/编辑（update）为同一套 jsp 模版，所以用 isCreate =
									 * true 标识为创建，以便与 update 区分开来。
									 */
		return String.format(jsp_adminInfo, service.getTableName());
	}

	/**
	 * 
	 * @param entity
	 * @param model
	 *            页面 Model 模型
	 * @return
	 */
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

	/**
	 * 
	 * @param entity
	 * @param model
	 *            页面 Model 模型
	 * @return
	 */
	public String update(/* @Valid */T entity, ModelAndView model) {
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", entity);
		model.put("isUpdate", true);
		initDb();

		try {
			getService().update(entity);
		} catch (ServiceException e) {
			model.put(errMsg, e);
		} finally {
			closeDb();
		}

		return cud;
	}

	/**
	 * 
	 * @param entry
	 * @param model
	 *            页面 Model 模型
	 * @return
	 */
	public String delete(T entry, ModelAndView model) {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", entry);
		initDb();

		try {
			if (!getService().delete(entry))
				throw new ServiceException("删除失败！");
		} catch (ServiceException e) {
			model.put(errMsg, e);
		} finally {
			closeDb();
		}

		return cud;
	}

	/**
	 * 
	 * @param id
	 * @param model
	 *            页面 Model 模型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String delete(ID id, ModelAndView model) {
		T obj = null;
		if (obj instanceof Map) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			obj = (T) map;
		} else {
			throw new RuntimeException("因为范型的缘故，不能实例化 bean 对象。应该在子类实例化 bean，再调用本类的 delete(T entry, ModelAndView model) ");
		}

		return delete(obj, model);
	}

	/**
	 * 
	 * @param result
	 * @return JSON 结果
	 */
	public static String outputListMapAsJson(List<Map<String, Object>> result) {
		if (result != null && result.size() > 0)
			return "json::{\"result\":" + JsonHelper.stringifyListMap(result) + "}";
		else
			return "json::{\"result\": null}";
	}

	/**
	 * 
	 * @param result
	 *            BaseMolde 集合
	 * @return JSON 结果
	 */
	public static String outputListBeanAsJson(List<? extends BaseModel> result) {
		if (result != null && result.size() > 0) {
			String[] str = new String[result.size()];

			for (int i = 0; i < result.size(); i++)
				str[i] = JsonHelper.bean2json((Object) result.get(i));

			return "json::{\"result\":[" + StringUtil.stringJoin(str, ",") + "]}";
		} else
			return "json::{\"result\": null}";
	}

	/**
	 * 显示 HTTP 405 禁止操作
	 * 
	 * @return HTTP 405 禁止操作 的 JSON
	 */
	public static String show405() {
		return String.format(json_not_ok, "405， Request method not supported 禁止操作");
	}

	public S getService() {
		if (service == null)
			throw new NullPointerException("没有业务层对象！");
		return service;
	}

	public void setService(S service) {
		if (service == null)
			LOGGER.warning("当前没有 service 对象传入！！！");
		this.service = service;
	}
}
