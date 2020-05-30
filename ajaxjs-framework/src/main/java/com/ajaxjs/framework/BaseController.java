package com.ajaxjs.framework;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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
 * @author sp42 frank@ajaxjs.com
 *
 * @param <T> 实体类型，可以为 Map 或 Java Bean
 */
public abstract class BaseController<T> implements IController, Constant {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseController.class);

	public abstract IBaseService<T> getService();

	/**
	 * 指向新建记录的页面
	 * 
	 * @param mv 页面 Model 模型
	 * @return 新建记录 UI JSP 模版路径
	 */
	public String createUI(ModelAndView mv) {
		return ui(mv, true, "新建");
	}

	/**
	 * 指向编辑记录的页面
	 * 
	 * @param mv   页面 Model 模型
	 * @param bean 实体
	 * @return JSP 模版路径
	 */
	public String editUI(ModelAndView mv, Object bean) {
		mv.put(INFO, bean);// 读取单个记录或者编辑某个记录，保存到 ModelAndView 中（供视图渲染用）。
		return ui(mv, false, "修改");
	}

	/**
	 * 
	 * @param mv       页面 Model 模型
	 * @param isCreate 因为新建/编辑（update）为同一套 jsp 模版，所以用 isCreate = true 标识为创建，以便与
	 *                 update 区分开来。
	 * @param text
	 * @return JSP 模版路径
	 */
	private String ui(ModelAndView mv, boolean isCreate, String text) {
		LOGGER.info(text + "记录 UI");

		prepareData(mv);
		mv.put("isCreate", isCreate);
		mv.put("actionName", text);

		return editUI();
	}

	/**
	 * 创建实体
	 * 
	 * @param bean 实体
	 * @return 新建实体之 id
	 */
	public String create(T bean) {
		Long newlyId = getService().create(bean);
		if (newlyId == null)
			throw new Error("创建失败！");

		return jsonOk_Extension("创建实体成功", "\"newlyId\":" + newlyId);
	}

	/**
	 * 设置实体之 id
	 * 
	 * @param id   实体 id
	 * @param bean 实体
	 */
	@SuppressWarnings("unchecked")
	private void setId(Long id, T bean) {
		if (bean instanceof BaseModel)
			((BaseModel) bean).setId(id);
		else if (bean instanceof Map)
			((Map<String, Object>) bean).put("id", id);
		else
			LOGGER.warning("未知实体类型 " + bean.getClass().getName());
	}

	/**
	 * 修改实体
	 * 
	 * @param id   实体 id
	 * @param bean 实体
	 * @return JSON 响应
	 */
	public String update(Long id, T bean) {
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", bean);

		setId(id, bean);
		getService().update(bean);

		return jsonOk("修改成功");
	}

	/**
	 * 根据 id 删除实体
	 * 
	 * @param id   实体 id
	 * @param bean 实体
	 * @return JSON 响应
	 */

	public String delete(Long id, T bean) {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", bean);

		setId(id, bean);

		if (!getService().delete(bean))
			throw new Error("删除失败！");

		return jsonOk("删除成功");
	}

	/**
	 * 可覆盖的模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param mv Model 模型
	 */
	public void prepareData(ModelAndView mv) {
		if (mv != null && getService() != null) {
			mv.put("uiName", getService().getUiName());
			mv.put("shortName", getService().getShortName());
			mv.put("tableName", getService().getTableName());
		}
	}

	/**
	 * 
	 * @param mv         Model 模型
	 * @param pageResult 分页数据
	 * @param isAdmin    是否后台列表
	 * @return
	 */
	public String page(ModelAndView mv, PageResult<T> pageResult, boolean isAdmin) {
		prepareData(mv);
		mv.put(PAGE_RESULT, pageResult);

		return list(isAdmin);
	}

	/**
	 * 自动根据客户端 HTTP 头传入的 Accept 字段决定是否输出 JSON 格式
	 * 
	 * @param mv         Model 模型
	 * @param pageResult 分页数据
	 * @param isAdmin    是否后台列表
	 * @return
	 */
	@Deprecated
	public String autoOutput(ModelAndView mv, PageResult<T> pageResult, boolean isAdmin) {
		if (isJson()) {
			return toJson(pageResult);
		} else
			return page(mv, pageResult, isAdmin);
	}
	@Deprecated
	public String autoOutput(ModelAndView mv, PageResult<T> pageResult, String jspPath) {
		if (isJson()) {
			return toJson(pageResult);
		} else {
			prepareData(mv);
			mv.put(PAGE_RESULT, pageResult);

			return jspPath;
		}
	}

	@Deprecated
	public String autoOutput(ModelAndView mv, BaseModel bean, String jspPath) {
		if (isJson()) {
			return toJson(bean);
		} else {
			mv.put(INFO, bean);

			return jspPath;
		}
	}

	public static String autoOutput(Object entity, ModelAndView mv, String jspPath) {
		return autoOutput(entity, mv, jspPath, null);
	}

	public static String autoOutput(Object entity, ModelAndView mv, String jspPath, Function<Boolean, Boolean> fn) {
		if (fn != null && fn.apply(isJson()) || (fn == null && isJson())) {
			if (entity instanceof PageResult)
				return toJson((PageResult<?>) entity);
			else
				return toJson(entity);
		} else {
			if (entity instanceof PageResult)
				mv.put(PAGE_RESULT, entity);

			if (entity instanceof Map || entity instanceof BaseModel)
				mv.put(INFO, entity);

			return jspPath;
		}
	}

	private static boolean isJson() {
		String accept = MvcRequest.getHttpServletRequest().getHeader("Accept");
		return accept != null && "application/json".equals(accept);

	}

	public String autoOutput(ModelAndView mv, Map<String, Object> bean, String jspPath) {

		if (isJson()) {
			return toJson(bean);
		} else {
			mv.put(INFO, bean);

			return jspPath;
		}
	}

	public String page(ModelAndView mv, PageResult<T> pageResult) {
		return page(mv, pageResult, CommonConstant.UI_ADMIN);
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
	 * @param obj   普通对象
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
		return String.format(JSON_OK, JsonHelper.javaValue2jsonValue(msg));
	}

	public static String jsonOk() {
		return jsonOk("操作成功！");
	}

	public static String jsonOk_Extension(Object... msg) {
		return String.format(JSON_OK_EXTENSION, msg);
	}

	/**
	 * 输出 JSON No OK
	 * 
	 * @param msg 输出信息
	 * @return JSON 字符串
	 */
	public static String jsonNoOk(String msg) {
		return String.format(JSON_NOT_OK, JsonHelper.jsonString_covernt(msg));
	}

	public static String jsonNoOk() {
		return jsonNoOk("操作失败！");
	}

	/**
	 * 显示 HTTP 405 禁止操作
	 */
	public static final String SHOW_405 = jsonNoOk("405， Request method not supported 禁止操作");

	/**
	 * 显示 HTTP 401 没有权限
	 */
	public static final String SHOW_401 = jsonNoOk("401， Request method not supported 没有权限");

	/**
	 * 
	 * @param jsp
	 * @return
	 */
	public static String jsp(String jsp) {
		return JSP_PERFIX_WEBINF + "/" + jsp;
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

	public static UploadFileInfo uploadByConfig(MvcRequest request, Consumer<UploadFileInfo> fn) throws IOException {
		UploadFileInfo info = new UploadFileInfo();
		info.isFileOverwrite = ConfigService.getValueAsBool("uploadFile.isFileOverwrite");
		info.saveFolder = ConfigService.getValueAsBool("uploadFile.saveFolder.isUsingRelativePath")
				? request.mappath(ConfigService.getValueAsString("uploadFile.saveFolder.relativePath")) + File.separator
				: ConfigService.getValueAsString("uploadFile.saveFolder.absolutePath");

		if (ConfigService.getValueAsBool("uploadFile.isAutoNewFileName"))
			info.saveFileName = new SnowflakeIdWorker(0, 0).nextId() + "";

		if (fn != null)
			fn.accept(info);

		new UploadFile(request, info).upload();

		info.path = ConfigService.getValueAsString("uploadFile.saveFolder.relativePath") + "/" + info.saveFileName;
		info.visitPath = request.getContextPath() + info.path;

		return info;
	}

	/**
	 * 执行文件上传，读取默认配置的上传规则
	 * 
	 * @param request 请求对象
	 * @return 上传结果对象
	 * @throws IOException
	 */
	public static UploadFileInfo uploadByConfig(MvcRequest request) throws IOException {
		return uploadByConfig(request, null);
	}

}
