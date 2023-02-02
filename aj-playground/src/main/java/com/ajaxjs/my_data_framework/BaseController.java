package com.ajaxjs.my_data_framework;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.map.JsonHelper;

/**
 * 一些通用的控制器方法
 *
 * @author Frank Cheung
 */
public abstract class BaseController {
	public static final String ID = "id";

	public static final String ID_INFO = "{id}";

	/**
	 * 指定 JSON 格式输出
	 */
	public static final String JSON = "application/json;charset=utf-8";

	/**
	 * 操作失败，返回 msg 信息
	 */
	public static final String JSON_NOT_OK = "{\"isOk\": false, \"msg\" : \"%s\"}";

	/**
	 * 操作成功，返回 msg 信息，可扩展字段的
	 */
	public static final String JSON_OK_EXTENSION = "{\"isOk\": true, \"msg\" : \"%s\", %s}";

	/**
	 * 操作成功，返回 msg 信息
	 */
	public static final String JSON_OK = "{\"isOk\": true, \"msg\" : \"%s\"}";

	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 创建一个实体
	 *
	 * @param service 业务对象
	 * @param bean    bean 实体
	 * @param <T>     bean 类型
	 * @return 创建好的 Bean
	 */
	public <T extends BaseModel> String create(IBaseService<T> service, T bean) {
		return afterCreate(service.create(bean));
	}

	/**
	 * 创建一个实体（Map 版本）
	 *
	 * @param service
	 * @param bean
	 * @return
	 */
	public String create(IBaseService<Map<String, Object>> service, Map<String, Object> bean) {
		return afterCreate(service.create(bean));
	}

	/**
	 * 创建实体
	 *
	 * @param newlyId 实体
	 * @return JSON 结果，包含新建实体之 id
	 */
	public static String afterCreate(Long newlyId) {
		if (newlyId == null)
			throw new Error("创建失败！");

		return jsonOk_Extension("创建实体成功", "\"newlyId\":" + newlyId);
	}

	public static <T extends BaseModel> T afterCreate(Long newlyId, T bean) {
		if (newlyId == null)
			throw new Error("创建失败！");

		bean.setId(newlyId);

		return bean;
	}

	/**
	 * 修改一个实体
	 *
	 * @param <T>
	 * @param service
	 * @param id
	 * @param bean
	 * @return
	 */
	public <T extends BaseModel> String update(IBaseService<T> service, long id, T bean) {
		bean.setId(id);

		return afterUpdate(service.update(bean));
	}

	/**
	 * 修改一个实体（Map 版本）
	 *
	 * @param service
	 * @param id
	 * @return
	 */
	public String update(IBaseService<Map<String, Object>> service, long id, Map<String, Object> bean) {
		bean.put(ID, id);

		return afterUpdate(service.update(bean));
	}

	/**
	 * 修改后响应的 JSON
	 *
	 * @param effectedRows
	 * @return
	 */
	public static String afterUpdate(int effectedRows) {
		if (effectedRows == 0 || effectedRows < 1)
			throw new Error("修改失败！");

		return jsonOk("修改成功");
	}

	/**
	 * 删除一个实体
	 *
	 * @param <T>
	 * @param service
	 * @param id
	 * @param bean
	 * @return
	 */
	public <T extends BaseModel> String delete(IBaseService<T> service, long id, T bean) {
		bean.setId(id);

		return afterDelete(service.delete(bean));
	}

	/**
	 * 删除一个实体（Map 版本）
	 *
	 * @param service
	 * @param id
	 * @return
	 */
	public String delete(IBaseService<Map<String, Object>> service, long id) {
		Map<String, Object> bean = new HashMap<>();
		bean.put(ID, id);

		return afterDelete(service.delete(bean));
	}

	/**
	 * 删除后响应的 JSON
	 *
	 * @param result
	 * @return
	 */
	public static String afterDelete(boolean result) {
		if (!result)
			throw new Error("删除失败！");

		return jsonOk("删除成功");
	}

	/**
	 * 判断是否期望 JSON 的结果
	 *
	 * @return true 表示为希望是 JSON
	 */
	public static boolean isJson() {
		String accept = getRequest().getHeader("Accept");
		return "application/json".equals(accept);
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
	 * @param obj          普通对象
	 * @param isAddPrefiex 是否添加生成 json 的前缀 json::。有时候在页面上输出 json 作为 JS 代码的一部分，就要设为
	 *                     false。
	 * @param isAddResult  JSON 是否包裹在 result 下面
	 * @return JSON 字符串
	 */
	public static String toJson(Object obj, boolean isAddPrefiex, boolean isAddResult) {
		String jsonStr = JsonHelper.toJson(obj);

		if (isAddResult)
			jsonStr = "{\"result\":" + jsonStr + "}";

//		if (isAddPerfix)
//			jsonStr = "json::" + jsonStr;

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
	 * @return 操作成功的 JSON 字符串
	 */
	public static String jsonOk() {
		return jsonOk("操作成功！");
	}

	/**
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

	/**
	 * 输出 JSON 操作失败
	 *
	 * @return
	 */
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
}
