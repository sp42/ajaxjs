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
package com.ajaxjs.simpleApp;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.util.SnowflakeIdWorker;
import com.ajaxjs.util.collection.MappingHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.BeanUtil;
import com.ajaxjs.web.UploadFile;
import com.ajaxjs.web.UploadFileInfo;

/**
 * 
 * 封装常见的控制器方法。注意：不能复用 create/update 方法，这是因为 T 泛型不能正确识别 Bean 类型的缘故 需要有 Service 层
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 * @param <T> 实体类型，可以是 Bean（POJO） 或 Map
 * @param <ID> ID 类型，可以是 INTEGER/LONG/String
 * @param <S> 业务类型
 */
public abstract class CommonController<T, ID extends Serializable, S extends IService<T, ID>> extends MappingHelper implements IController, Constant {
	private static final LogHelper LOGGER = LogHelper.getLog(CommonController.class);

	/**
	 * 新建记录 UI
	 * 
	 * @param model 页面 Model 模型
	 * @return 新建记录 UI JSP 模版路径
	 */
	public String createUI(ModelAndView model) {
		LOGGER.info("新建记录 UI");

		prepareData(model);
		model.put("actionName", "新建");
		model.put("isCreate", true); // 因为新建/编辑（update）为同一套 jsp 模版，所以用 isCreate
										// = true 标识为创建，以便与 update 区分开来。

		return getService().getTableName();
	}

	/**
	 * 编辑记录 UI
	 * 
	 * @param model 页面 Model 模型
	 * @return 编辑记录 UI JSP 模版路径
	 */
	public String editUI(ModelAndView model) {
		LOGGER.info("编辑记录 UI");

		prepareData(model);
		model.put("actionName", "编辑");
		model.put("isCreate", false); // 因为新建/编辑（update）为同一套 jsp 模版，所以用 isCreate
										// = true 标识为创建，以便与 update 区分开来。

		return getService().getTableName();
	}

	/**
	 * 创建实体
	 * 
	 * @param entity 实体
	 * @param model 页面 Model 模型
	 * @return JSON 响应
	 * @throws ServiceException
	 */
	public String create(T entity, ModelAndView model) throws ServiceException {
		LOGGER.info("创建 name:{0}，数据库将执行 INSERT 操作", entity);

		prepareData(model);

		ID newlyId = getService().create(entity);
		if (newlyId == null)
			throw new ServiceException("创建失败！");

		model.put("newlyId", newlyId);
		return cud;
	}

	/**
	 * 修改实体
	 * 
	 * @param id 实体 ID
	 * @param entity 实体
	 * @param model 页面 Model 模型
	 * @return JSON 响应
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public String update(ID id, /* @Valid */T entity, ModelAndView model) throws ServiceException {
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", entity);

		prepareData(model);
		model.put("isUpdate", true);

		if (entity instanceof Map) {
			((Map<String, Object>) entity).put("id", id);
		} else {
			System.out.println(id);
			((BaseModel) entity).setId((Long) id);
		}

		getService().update(entity);

		return cud;
	}

	/**
	 * 
	 * 因为范型的缘故，不能实例化 bean 对象。应该在子类实例化 bean，再调用本类的 delete()
	 * 
	 * @param entity 实体
	 * @param model 页面 Model 模型
	 * @return JSON 响应
	 */
	public String delete(T entity, ModelAndView model) throws ServiceException {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", entity);

		if (!getService().delete(entity))
			throw new ServiceException("删除失败！");

		return jsonOk("删除成功");
	}

	/**
	 * 根据 id 删除实体
	 * 
	 * @param id 实体 id
	 * @param model 页面 Model 模型
	 * @return JSON 响应
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public String delete(ID id, ModelAndView model) throws ServiceException {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", id);
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);

		return delete((T) map, model);
	}

	/**
	 * 读取单个记录或者编辑某个记录，保存到 ModelAndView 中（供视图渲染用）。
	 * 
	 * @param id ID 序号
	 * @param model Model 模型
	 * @return JSP 路径。缺省提供一个默认路径，但不一定要使用它，换别的也可以。
	 * @throws ServiceException
	 */
	public String info(ID id, ModelAndView model) throws ServiceException {
		LOGGER.info("读取单个记录或者编辑某个记录：id 是 {0}", id);

		prepareData(model);
		model.put("info", getService().findById(id));

		return service.getTableName();
	}

	/**
	 * 分页查询
	 * 
	 * @param start 起始行数，默认从零开始
	 * @param limit 偏量值，默认 8 笔记录
	 * @param model Model 模型
	 * @return JSP 路径。缺省提供一个默认路径，但不一定要使用它，换别的也可以。
	 * @throws ServiceException
	 */
	public String list(int start, int limit, ModelAndView model) throws ServiceException {
		LOGGER.info("获取分页列表 GET list:{0}/{1}", start, limit);

		prepareData(model);

		PageResult<T> pageResult = getService().findPagedList(start, limit);
		model.put("PageResult", pageResult);

		return null;
	}

	public static <E> void pageList(PageResult<E> pageResult, ModelAndView model) {
		model.put("PageResult", pageResult);
	}

	/**
	 * 将分页列表转化为 JSON 输出
	 * 
	 * @param start
	 * @param limit
	 * @param model
	 * @return
	 * @throws ServiceException
	 */
	public String listJson(int start, int limit, ModelAndView model) throws ServiceException {
		LOGGER.info("获取分页列表 GET list:{0}/{1}", start, limit);

		prepareData(model);

		PageResult<T> pageResult = getService().findPagedList(start, limit);
		model.put("PageResult", pageResult);

		return outputJson(pageResult, model);
	}

	/**
	 * 获取全部列表数据
	 * 
	 * @param model Model 模型
	 * @throws ServiceException
	 */
	public void list_all(ModelAndView model) throws ServiceException {
		LOGGER.info("----获取全部列表----");
		list(0, 999, model);
	}

	/**
	 * 可覆盖的模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param model 模型
	 */
	public void prepareData(ModelAndView model) {
		if (service != null) {
			// 设置实体 id 和 现实名称 。
			model.put("uiName", service.getName());
			model.put("tableName", service.getTableName());
		}
	}

	/**
	 * 
	 * @return
	 */
	public static QueryParams getParam() {
		HttpServletRequest request = MvcRequest.getHttpServletRequest();
		return new QueryParams(request.getParameterMap());
	}

	/**
	 * 保存到 request
	 * 
	 * @param request 请求对象
	 */
	public static void saveToReuqest(ModelAndView mv, HttpServletRequest request) {
		for (String key : mv.keySet())
			request.setAttribute(key, mv.get(key));
	}


	@SuppressWarnings("unchecked")
	public String outputJson(PageResult<T> pageResult, ModelAndView model) {
		String jsonStr = "[]"; // empty array
		if (pageResult != null && pageResult.size() > 0) {

			if (pageResult.get(0) instanceof Map) { // Map 类型的输出
				List<Map<String, Object>> list = (List<Map<String, Object>>) pageResult;
				jsonStr = JsonHelper.stringifyListMap(list);
			} else { // Bean
				jsonStr = BeanUtil.listToJson((List<Object>) pageResult);
			}
		}

		model.put("MapOutput", jsonStr);

		return paged_json_List;
	}
	
	/**
	 * 执行文件上传，读取默认配置的上传规则
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static UploadFileInfo uploadByConfig(MvcRequest request) throws IOException {
		UploadFileInfo info = new UploadFileInfo();
		info.isFileOverwrite = ConfigService.getValueAsBool("uploadFile.isFileOverwrite");
		info.saveFolder = ConfigService.getValueAsBool("uploadFile.saveFolder.isUsingRelativePath") ? request.mappath(ConfigService.getValueAsString("uploadFile.saveFolder.relativePath")) + File.separator
				: ConfigService.getValueAsString("uploadFile.saveFolder.absolutePath");

		if (ConfigService.getValueAsBool("uploadFile.isAutoNewFileName")) {
			info.saveFileName = new SnowflakeIdWorker(0, 0).nextId() + "";
		}

		new UploadFile(request, info).upload();

		info.path = ConfigService.getValueAsString("uploadFile.saveFolder.relativePath") + "/" + info.saveFileName;
		info.visitPath = request.getContextPath() + info.path;

		return info;
	}

	/**
	 * 对应的业务类
	 */
	private S service;

	/**
	 * 获取业务对象
	 * 
	 * @return 业务对象
	 */
	public S getService() {
		if (service == null)
			throw new NullPointerException("没有业务层对象！");
		return service;
	}

	/**
	 * 设置业务对象，通常由 IOC 反射调用
	 * 
	 * @param service 业务对象
	 */
	public void setService(S service) {
		if (service == null)
			LOGGER.warning("当前没有 service 对象传入！！！");
		this.service = service;
	}
	
}
