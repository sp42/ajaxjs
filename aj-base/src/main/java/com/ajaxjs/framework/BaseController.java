/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.mvc.IController;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.MvcConstant;
import com.ajaxjs.web.mvc.MvcRequest;

/**
 * 通用控制器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 * @param <T> 实体类型，可以为 Map 或 Java Bean
 */
public abstract class BaseController<T> implements IController, MvcConstant {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseController.class);

	/**
	 * 服务对象
	 * 
	 * @return 服务对象
	 */
	public abstract IBaseService<T> getService();

	/**
	 * JSP 都保存在 /WEB-IMF/jsp/ 下面。再不断地分类
	 * 
	 * @param jsp 模板位置
	 * @return JSP 文件路径
	 */
	public static String jsp(String jsp) {
		return JSP_PERFIX_WEBINF + "/" + jsp;
	}

	/**
	 * 普通页面模板目录
	 * 
	 * @param jsp 模板位置
	 * @return JSP 文件路径
	 */
	public static String page(String jsp) {
		return jsp("pages/" + jsp);
	}

	/**
	 * 后台用页面模板目录
	 * 
	 * @param jsp 模板位置
	 * @return JSP 文件路径
	 */
	public static String admin(String jsp) {
		return jsp("admin/" + jsp);
	}

	/**
	 * 可覆盖的模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param mv Model 模型
	 */
	public void prepareData(ModelAndView mv) {
		IBaseService<T> service = getService();

		if (mv != null && service != null) {
			mv.put("uiName", service.getUiName());
			mv.put("shortName", service.getShortName());
			mv.put("tableName", service.getTableName());
		}
	}

	/**
	 * 新建记录的页面，必定是 MVC 而不是 JSON 的
	 * 
	 * @param mv 页面 Model 模型
	 * @return JSP 文件标识，通常是如 news-edit
	 */
	public String createUI(ModelAndView mv) {
		mv.put("isCreate", true);// 因为新建/编辑（update）为同一套 jsp 模版，所以用 isCreate = true 标识为创建，以便与 update 区分开来。
		prepareData(mv);

		return getService().getShortName();
	}

	/**
	 * 创建实体
	 * 
	 * @param bean 实体
	 * @return JSON 结果，包含新建实体之 id
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
			((Map<String, Object>) bean).put(ID, id);
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
	 * 自动根据客户端 HTTP 头传入的 Accept 字段决定是否输出 JSON 格式
	 * 
	 * @param entity  实体，可以是单个实体或者列表、分页列表
	 * @param mv      Model 模型
	 * @param jspPath JSP 模板路径
	 * @return 结果，可能是页面，也可能是 JSON
	 */
	public static String autoOutput(Object entity, ModelAndView mv, String jspPath) {
		return autoOutput(entity, mv, jspPath, null);
	}

	/**
	 * 自动根据客户端 HTTP 头传入的 Accept 字段决定是否输出 JSON 格式
	 * 
	 * @param entity  实体，可以是单个实体或者列表、分页列表
	 * @param mv      Model 模型
	 * @param jspPath JSP 模板路径
	 * @param fn      输入是否期望 JSON, 是否期望 JSON 的 lambda
	 * @return 结果，可能是页面，也可能是 JSON
	 */
	public static String autoOutput(Object entity, ModelAndView mv, String jspPath, Function<Boolean, Boolean> fn) {
		if (fn != null && fn.apply(isJson()) || (fn == null && isJson())) {
			if (entity instanceof PageResult)
				return toJson((PageResult<?>) entity);
			else
				return toJson(entity);
		} else {
			if (entity instanceof PageResult)
				mv.put(PAGE_RESULT, entity);// 读取单个记录或者编辑某个记录，保存到 ModelAndView 中（供视图渲染用）。

			if (entity instanceof Map || entity instanceof BaseModel)
				mv.put(INFO, entity);

			if (jspPath.startsWith("jsp::"))
				return jsp(jspPath.replace("jsp::", ""));
			if (jspPath.startsWith("page::"))
				return page(jspPath.replace("page::", ""));
			if (jspPath.startsWith("admin::"))
				return admin(jspPath.replace("admin::", ""));
			else
				return jspPath;
		}
	}

	public String output(ModelAndView mv, Object entityOrId) {
		String name = getService().getShortName();
		String jspPath = entityOrId instanceof PageResult ? name + "-list" : name;

		return output(mv, entityOrId, jspPath);
	}

	public String output(ModelAndView mv, Object entityOrId, String jspPath) {
		return output(mv, entityOrId, jspPath, null);
	}

	@SuppressWarnings("unchecked")
	public String output(ModelAndView mv, Object entityOrId, String jspPath, Consumer<T> onGetInfo) {
		if (entityOrId instanceof Long)
			entityOrId = getService().findById((long) entityOrId);

		if (onGetInfo != null)
			onGetInfo.accept((T) entityOrId);

		prepareData(mv);

		return autoOutput(entityOrId, mv, jspPath);
	}

	/**
	 * 判断是否期望 JSON 的结果
	 * 
	 * @return true 表示为希望是 JSON
	 */
	public static boolean isJson() {
		String accept = MvcRequest.getHttpServletRequest().getHeader("Accept");
		return accept != null && "application/json".equals(accept);
	}

	/**
	 * 把 Bean/Map/List 转换为 JSON
	 * 
	 * @param obj Bean/Map/List
	 * @return JSON 结果
	 */
	public static String toJson(Object obj) {
		return toJson(obj, true, true);
	}

	/**
	 * 将 Object 转换为 JSON 字符串
	 * 
	 * @param obj         普通对象
	 * @param isAddPerfix 是否添加生成 json 的前缀 json::。有时候在页面上输出 json 作为 JS 代码的一部分，就要设为
	 *                    false。
	 * @param isAddResult JSON 是否包裹在 result 下面
	 * @return JSON 字符串
	 */
	public static String toJson(Object obj, boolean isAddPerfix, boolean isAddResult) {
		String jsonStr = JsonHelper.toJson(obj);

		if (isAddResult)
			jsonStr = "{\"result\":" + jsonStr + "}";

		if (isAddPerfix)
			jsonStr = "json::" + jsonStr;

		return jsonStr;
	}

	/**
	 * 分页转为 JSON
	 * 
	 * @param pageResult
	 * @return JSON 字符串
	 */
	public static String toJson(PageResult<?> pageResult) {
		String jsonStr = toJson(pageResult, true, false);

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

	/**
	 * 输出 JSON 操作成功
	 * 
	 * @return
	 */
	public static String jsonOk() {
		return jsonOk("操作成功！");
	}

	/**
	 * 
	 * @param msg
	 * @return
	 */
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

//	/**
//	 * 
//	 * @param request
//	 * @param fn
//	 * @return
//	 * @throws IOException
//	 */
//	public static UploadFileInfo uploadByConfig(MvcRequest request, Consumer<UploadFileInfo> fn) throws IOException {
//		UploadFileInfo info = new UploadFileInfo();
//		info.isFileOverwrite = ConfigService.getBol("uploadFile.isFileOverwrite");
//		info.saveFolder = ConfigService.getBol("uploadFile.saveFolder.isUsingRelativePath")
//				? request.mappath(ConfigService.get("uploadFile.saveFolder.relativePath")) + File.separator
//				: ConfigService.get("uploadFile.saveFolder.absolutePath");
//
//		if (ConfigService.getBol("uploadFile.isAutoNewFileName"))
//			info.saveFileName = new SnowflakeIdWorker(0, 0).nextId() + "";
//
//		if (fn != null)
//			fn.accept(info);
//
//		new UploadFile(request, info).upload();
//
//		info.path = ConfigService.get("uploadFile.saveFolder.relativePath") + "/" + info.saveFileName;
//		info.visitPath = request.getContextPath() + info.path;
//
//		return info;
//	}
//
//	/**
//	 * 执行文件上传，读取默认配置的上传规则
//	 * 
//	 * @param request 请求对象
//	 * @return 上传结果对象
//	 * @throws IOException
//	 */
//	public static UploadFileInfo uploadByConfig(MvcRequest request) throws IOException {
//		return uploadByConfig(request, null);
//	}

}
