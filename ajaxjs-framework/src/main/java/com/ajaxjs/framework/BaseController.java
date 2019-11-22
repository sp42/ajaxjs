package com.ajaxjs.framework;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.SnowflakeIdWorker;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.UploadFile;
import com.ajaxjs.web.UploadFileInfo;

/**
 * 通用控制器
 * 
 * @author Administrator
 *
 * @param <T>
 */
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
	 * @param id
	 * @param mv 页面 Model 模型
	 * @return
	 */
	public String editUI(Long id, ModelAndView mv) {
		info(id, mv, getService());
		return ui(mv, false, "修改");
	}

	public String editUI(Long id, ModelAndView mv, Function<Long, T> getInfoAction) {
		info(id, mv, getInfoAction);
		return ui(mv, false, "修改");
	}

	/**
	 * 
	 * @param mv
	 * @param isCreate
	 * @param text
	 * @return
	 */
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
	 * @param entry 实体
	 * @param createAction
	 * @return
	 */
	public static <E> String create(E entry, Function<E, Long> createAction) {
		LOGGER.info("创建 name:{0}，数据库将执行 INSERT 操作");

		Long newlyId = createAction.apply(entry);

		if (newlyId == null)
			throw new RuntimeException("创建失败！");

		return jsonOk_Extension("创建实体成功", "\"newlyId\":" + newlyId);
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

	/**
	 * 
	 * @param id ID 序号
	 * @param mv Model 模型
	 * @param service
	 * @return
	 */
	public <E> E info(Long id, ModelAndView mv, IBaseService<E> service) {
		return info(id, mv, service::findById);
	}

	/**
	 * 
	 * @param id ID 序号
	 * @param mv Model 模型
	 * @return
	 */
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

		if (entity instanceof BaseModel)
			((BaseModel) entity).setId(id);
		else if (entity instanceof Map)
			((Map<String, Object>) entity).put("id", id);
		else
			LOGGER.warning("未知实体类型 " + entity.getClass().getName());

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

		if (entity instanceof BaseModel)
			((BaseModel) entity).setId(id);
		else if (entity instanceof Map)
			((Map<String, Object>) entity).put("id", id);
		else
			LOGGER.warning("未知实体类型 " + entity.getClass().getName());

		if (!delAction.test(entity))
			throw new Error("删除失败！");

		return jsonOk("删除成功");
	}

	/**
	 * 
	 * @param id
	 * @param entity
	 * @param service
	 * @return
	 */
	public static <E> String delete(Long id, E entity, IBaseService<E> service) {
		return delete(id, entity, service::delete);
	}

	/**
	 * 
	 * @param id
	 * @param entity
	 * @return
	 */
	public String delete(Long id, T entity) {
		return delete(id, entity, getService());
	}

	/**
	 * 可覆盖的模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param mv Model 模型
	 */
	public void prepareData(ModelAndView mv) {
		if (getService() != null) {
			mv.put("uiName", getService().getUiName());
			mv.put("shortName", getService().getShortName());
			mv.put("tableName", getService().getTableName());
		}
	}

	/**
	 * 
	 * @param mv
	 * @param pageResult
	 * @param isAdmin 是否后台列表
	 * @return
	 */
	public String page(ModelAndView mv, PageResult<T> pageResult, boolean isAdmin) {
		LOGGER.info("获取" + getService().getUiName() + "分页列表 GET list");

		prepareData(mv);
		mv.put(PageResult, pageResult);
		
		return list(isAdmin);
	}

	@FunctionalInterface
	static public interface PagedDao<T> {
		public PageResult<T> apply(Integer s, Integer l, Function<String, String> sqlHandler);
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

	/**
	 * 将 Object 转换为 JSON 字符串
	 * 
	 * @param obj 普通对象
	 * @param isAdd 是否添加生成 json 的前缀 json::
	 * @return JSON 字符串
	 */
	public static String toJson(Object obj, boolean isAdd) {
		String jsonStr = JsonHelper.toJson(obj);

		return isAdd ? "json::{\"result\":" + jsonStr + "}" : jsonStr;
	}

	/**
	 * 分页转为 JSON
	 * 
	 * @param pageResult
	 * @return
	 */
	public static String toJson(PageResult<?> pageResult) {
		String jsonStr = toJson(pageResult, false);

		if (jsonStr == null || pageResult == null)
			jsonStr = "[]";

		int total = pageResult == null || pageResult.isZero() ? 0 : pageResult.getTotalCount();

		return jsonOk_Extension("分页列表", "\"result\":" + jsonStr + ",\"total\":" + total);
	}

	/**
	 * 输出 JSON OK
	 * 
	 * @param msg 输出信息
	 * @return JSON 字符串
	 */
	public static String jsonOk(String msg) {
		return String.format(json_ok, JsonHelper.javaValue2jsonValue(msg));
	}

	public static String jsonOk_Extension(Object... msg) {
		return String.format(json_ok_extension, msg);
	}

	/**
	 * 输出 JSON No OK
	 * 
	 * @param msg 输出信息
	 * @return JSON 字符串
	 */
	public static String jsonNoOk(String msg) {
		return String.format(json_not_ok, JsonHelper.jsonString_covernt(msg));
	}


	/**
	 * 显示 HTTP 405 禁止操作
	 */
	public static final String show405 = jsonNoOk("405， Request method not supported 禁止操作");

	/**
	 * 显示 HTTP 401 没有权限
	 */
	public static final String show401 = jsonNoOk("401， Request method not supported 没有权限");

	/**
	 * 
	 * @param jsp
	 * @return
	 */
	public static String jsp(String jsp) {
		return jsp_perfix_webinf + "/" + jsp;
	}

	/**
	 * 普通页面模板目录
	 * 
	 * @param jsp
	 * @return
	 */
	public static String page(String jsp) {
		return jsp("pages/" + jsp);
	}
	
	/**
	 * 后台用页面模板目录
	 * 
	 * @param jsp
	 * @return
	 */
	public static String admin(String jsp) {
		return jsp("admin/" + jsp);
	}

	/**
	 * 
	 * @param isAdmin 是否后台列表
	 * @return
	 */
	public String list(boolean isAdmin) {
		return page(getService().getShortName() + (isAdmin ? "-admin-list" : "-list"));
	}

	public String info() {
		return page(getService().getShortName() + "-info");
	}

	public String editUI() {
		return page(getService().getShortName() + "-edit");
	}

	/**
	 * 输出 Excel XSL 格式文件
	 * 
	 * @param response 响应对象
	 * @param fileName 文件名
	 * @return
	 */
	public String adminList_Excel(HttpServletResponse response, String fileName) {
		String now = fileName + "-" + LocalDate.now().toString();

		try {
			now = new String(now.getBytes(), "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			LOGGER.warning(e);
		}
		// <base href="<%=basePath%>">
		// String basePath = request.getScheme() + "://" + request.getServerName() + ":"
		// + request.getServerPort() + request.getContextPath() + "/";

		// <%@ page pageEncoding="utf-8" contentType="application/msexcel"%>
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");

		response.setHeader("Content-Disposition", String.format("attachment; filename=%s.xls", now));
		// response.setHeader("Content-disposition","inline; filename=videos.xls");

		// 以上这行设定传送到前端浏览器时的档名为test.xls
		// 就是靠这一行，让前端浏览器以为接收到一个excel档

		return page(getService().getShortName() + "-admin-list-xsl");
	}

	/**
	 * 执行文件上传，读取默认配置的上传规则
	 * 
	 * @param request 请求对象
	 * @return 上传结果对象
	 * @throws IOException
	 */
	public static UploadFileInfo uploadByConfig(MvcRequest request) throws IOException {
		UploadFileInfo info = new UploadFileInfo();
		info.isFileOverwrite = ConfigService.getValueAsBool("uploadFile.isFileOverwrite");
		info.saveFolder = ConfigService.getValueAsBool("uploadFile.saveFolder.isUsingRelativePath") ? request.mappath(ConfigService.getValueAsString("uploadFile.saveFolder.relativePath")) + File.separator
				: ConfigService.getValueAsString("uploadFile.saveFolder.absolutePath");

		if (ConfigService.getValueAsBool("uploadFile.isAutoNewFileName"))
			info.saveFileName = new SnowflakeIdWorker(0, 0).nextId() + "";

		new UploadFile(request, info).upload();

		info.path = ConfigService.getValueAsString("uploadFile.saveFolder.relativePath") + "/" + info.saveFileName;
		info.visitPath = request.getContextPath() + info.path;

		return info;
	}

}
