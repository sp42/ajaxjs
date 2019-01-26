package com.ajaxjs.framework;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.thirdparty.SnowflakeIdWorker;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFile;
import com.ajaxjs.web.UploadFileInfo;

public abstract class BaseController<T> implements IController, Constant {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseController.class);

	public abstract IBaseService<T> getService();

	/**
	 * 指向新建记录的页面
	 * 
	 * @param model 页面 Model 模型
	 * @return 新建记录 UI JSP 模版路径
	 */
	public String createUI(ModelAndView mv) {
		return ui(mv, true, "新建");
	}

	/**
	 * 指向编辑记录的页面
	 * 
	 * @param mv 页面 Model 模型
	 * @return 编辑记录 UI JSP 模版路径
	 */
	public String editUI(Long id, ModelAndView mv) {
		info(id, mv, getService());
		return ui(mv, false, "修改");
	}

	public String editUI(Long id, ModelAndView mv, Function<Long, T> getInfoAction) {
		info(id, mv, getInfoAction);
		return ui(mv, false, "修改");
	}

	private String ui(ModelAndView mv, boolean isCreate, String text) {
		LOGGER.info(text + "记录 UI");

		prepareData(mv);
		mv.put("isCreate", isCreate); // 因为新建/编辑（update）为同一套 jsp 模版，所以用 isCreate = true 标识为创建，以便与 update 区分开来。
		mv.put("actionName", text);

		return editUI();
	}

	/**
	 * 创建实体
	 * 
	 * @param entity 实体
	 * @return JSON 响应
	 */
	public static <E> String create(E entry, Function<E, Long> createAction) {
		LOGGER.info("创建 name:{0}，数据库将执行 INSERT 操作");

		Long newlyId = createAction.apply(entry);
		if (newlyId == null)
			throw new RuntimeException("创建失败！");

		return String.format(JsonHelper.json_ok_extension, "创建实体成功", "\"newlyId\":" + newlyId);
	}

	public static <E> String create(E entry, IBaseService<E> service) {
		return create(entry, service::create);
	}

	public String create(T entry) {
		return create(entry, getService());
	}

	/**
	 * 读取单个记录或者编辑某个记录，保存到 ModelAndView 中（供视图渲染用）。
	 * 
	 * @param id ID 序号
	 * @param model Model 模型
	 * @return JSP 路径。缺省提供一个默认路径，但不一定要使用它，换别的也可以。
	 */
	public <E> E info(Long id, ModelAndView mv, Function<Long, E> getInfoAction) {
		LOGGER.info("读取单个记录或者编辑某个记录：id 是 {0}", id);

		E info = getInfoAction.apply(id);

		if (mv != null) {
			prepareData(mv);
			mv.put("info", info);
		}

		return info;
	}

	public <E> E info(Long id, ModelAndView mv, IBaseService<E> service) {
		return info(id, mv, service::findById);
	}

	public T info(Long id, ModelAndView mv) {
		return info(id, mv, getService());
	}

	/**
	 * 修改实体
	 * 
	 * @param id 实体 Long
	 * @param entity 实体
	 * @return JSON 响应
	 */
	@SuppressWarnings("unchecked")
	public static <E> String update(Long id, E entity, Consumer<E> updateAction) {
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", entity);
		
		if (entity instanceof BaseModel) {
			((BaseModel) entity).setId(id);
		} else if (entity instanceof Map) {
			((Map<String, Object>) entity).put("id", id);
		} else {
			LOGGER.warning("未知实体类型 " + entity.getClass().getName());
		}

		updateAction.accept(entity);

		return jsonOk("修改成功");
	}

	public static <E> String update(Long id, E entry, IBaseService<E> service) {
		return update(id, entry, service::update);
	}

	public String update(Long id, T entity) {
		return update(id, entity, getService());
	}

	/**
	 * 根据 id 删除实体
	 * 
	 * @param id 实体 id
	 * @param model 页面 Model 模型
	 * @return JSON 响应
	 */
	@SuppressWarnings("unchecked")
	public static <E> String delete(Long id, E entity, Predicate<E> delAction) {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", entity);
		if (entity instanceof BaseModel) {
			((BaseModel) entity).setId(id);
		} else if (entity instanceof Map) {
			((Map<String, Object>) entity).put("id", id);
		} else {
			LOGGER.warning("未知实体类型 " + entity.getClass().getName());
		}

		if (!delAction.test(entity))
			throw new Error("删除失败！");

		return jsonOk("删除成功");
	}

	public static <E> String delete(Long id, E entity, IBaseService<E> service) {
		return delete(id, entity, service::delete);
	}

	public String delete(Long id, T entity) {
		return delete(id, entity, getService());
	}

	/**
	 * 可覆盖的模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param mv 模型
	 */
	public void prepareData(ModelAndView mv) {
		mv.put("uiName", getService().getUiName());
		mv.put("shortName", getService().getShortName());
		mv.put("tableName", getService().getTableName());
	}

	/**
	 * 分页查询
	 * 
	 * @param start 起始行数，默认从零开始
	 * @param limit 偏量值，默认 8 笔记录
	 * @param mv Model 模型
	 * @return JSP 路径。缺省提供一个默认路径，但不一定要使用它，换别的也可以。
	 */

	public <E> PageResult<E> listPaged(int start, int limit, ModelAndView mv, BiFunction<Integer, Integer, PageResult<E>> findPagedListAction) {
		LOGGER.info("获取分页列表 GET list");

		prepareData(mv);

		PageResult<E> pageResult = findPagedListAction.apply(start, limit);
		mv.put(PageResult, pageResult);

		return pageResult;
	}

	public <E> PageResult<E> listPaged(int start, int limit, ModelAndView mv, IBaseService<E> service) {
		return listPaged(start, limit, mv, service::findPagedList);
	}

	public PageResult<T> listPaged(int start, int limit, ModelAndView mv) {
		return listPaged(start, limit, mv, getService());
	}

	/**
	 * 把 Bean/Map/List 转换为 JSON
	 * 
	 * @param result Map
	 * @return JSON 结果
	 */
	public static String toJson(Object obj) {
		return toJson(obj, true);
	}

	public String toJson(PageResult<T> pageResult) {
		return pagedListJson(pageResult);
	}

	public static <E> String pagedListJson(PageResult<E> pageResult) {
		String jsonStr = toJson(pageResult, false);
		if (jsonStr == null)
			jsonStr = "[]";

		int total = pageResult.isZero() ? 0 : pageResult.getTotalCount();

		return String.format(JsonHelper.json_ok_extension, "分页列表", "\"result\":" + jsonStr + ",\"total\":" + total);
	}

	public static String toJson(Object obj, boolean isAdd) {
		String jsonStr = JsonHelper.toJson(obj);
		return isAdd ? "json::{\"result\":" + jsonStr + "}" : jsonStr;
	}

	public static String jsonOk(String msg) {
		return JsonHelper.jsonOk(msg);
	}

	public static String jsonNoOk(String msg) {
		return JsonHelper.jsonNoOk(msg);
	}

	public static String jsp(String jsp) {
		return jsp_perfix_webinf + "/" + jsp;
	}

	public static String cms(String jsp) {
		return jsp("cms/" + jsp);
	}

	public String adminListCMS() {
		return cms(getService().getShortName() + "-list");
	}

	public String editUI_CMS() {
		return cms(getService().getShortName());
	}

	public static final String domainEntityList = cms("common-entity-admin-list");

	public static final String domainEntityEdit = cms("common-entity");

	public static String info(String jsp) {
		return jsp("entry/" + jsp);
	}

	public static String list(String jsp) {
		return jsp(String.format("entry/%s-list", jsp));
	}

	public String editUI() {
		return info(getService().getShortName() + "-edit");
	}

	public String adminList() {
		return info(getService().getShortName() + "-admin-list");
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

}
