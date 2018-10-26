package com.ajaxjs.keyvalue;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.js.JsonHelper;

/**
 * 为 key-value 结构的数据提供数据转换的中间件
 * 
 * @author Frank Cheung
 *
 */
public class MappingHelper {

	private Map<String, Object> map;

	private BaseModel bean;

	private Object obj;

	private String json;

	private boolean addJsonPerfix;

	public MappingHelper(Map<String, Object> map) {
		this.map = map;
	}

	public MappingHelper(BaseModel bean) {
		this.bean = bean;
	}

	public MappingHelper(String json) {
		this.json = json;
	}

	public MappingHelper() {
	}

	/**
	 * 添加控制器能识别 “json::”
	 * 
	 * @return 当前实例以便链式调用
	 */
	public MappingHelper addJsonPerfix() {
		addJsonPerfix = true;
		return this;
	}

	/**
	 * 
	 * @param obj
	 * @return 当前实例以便链式调用
	 */
	public MappingHelper setObject(Object obj) {
		this.obj = obj;
		return this;
	}

	/**
	 * 转换为 JSON 字符串输出
	 * 
	 * @return JSON 字符串
	 */
	public String toJson() {
		if (map != null) {
			json = MappingJson.stringifyMap(map);
		} else if (bean != null) {
			json = BeanUtil.beanToJson(bean);
		} else if (obj != null) {
			json = MappingJson.stringifySimpleObject(obj);
		}

		if (json != null && addJsonPerfix)
			return "json::" + json;

		return json;
	}

	public Map<String, Object> toMap() {
		if (json != null) {
			return JsonHelper.parseMap(json);
		}

		return null;
	}

	/**
	 * 操作成功，返回 msg 信息
	 */
	public static final String json_ok = "json::{\"isOk\": true, \"msg\" : \"%s\"}";

	/**
	 * 操作成功，返回 msg 信息，可扩展字段的
	 */
	public static final String json_ok_extension = "json::{\"isOk\": true, \"msg\" : \"%s\", %s}";

	/**
	 * 操作失败，返回 msg 信息
	 */
	public static final String json_not_ok = "json::{\"isOk\": false, \"msg\" : \"%s\"}";

	/**
	 * 输出 JSON OK
	 * 
	 * @param msg
	 * @return
	 */
	public static String jsonOk(String msg) {
		return String.format(json_ok, msg);
	}

	/**
	 * 输出 JSON No OK
	 * 
	 * @param msg
	 * @return
	 */
	public static String jsonNoOk(String msg) {
		return String.format(json_not_ok, msg);
	}

	/**
	 * 把 Bean 转换为 JSON
	 * 
	 * @param bean bean
	 * @return JSON 结果
	 */
	public static String outputBeanAsJson(BaseModel bean) {
		if (bean != null)
			return "json::{\"result\":" + BeanUtil.beanToJson(bean) + "}";
		else
			return "json::{\"result\": null}";
	}

	/**
	 * 把 Map 转换为 JSON 数组
	 * 
	 * @param result Map
	 * @return JSON 结果
	 */
	public static String outputMapAsJson(Map<String, Object> result) {
		if (result != null)
			return "json::{\"result\":" + MappingJson.stringifyMap(result) + "}";
		else
			return "json::{\"result\": null}";
	}

	/**
	 * 把 Map 集合转换为 JSON 数组
	 * 
	 * @param result Map 集合
	 * @return JSON 结果
	 */
	public static String outputListMapAsJson(List<Map<String, Object>> result) {
		if (result != null && result.size() > 0)
			return "json::{\"result\":" + MappingJson.stringifyListMap(result) + "}";
		else
			return "json::{\"result\": null}";
	}

	/**
	 * 把 Bean 集合转换为 JSON 数组
	 * 
	 * @param result BaseMolde 集合
	 * @return JSON 结果
	 */
	public static String outputListBeanAsJson(List<? extends BaseModel> result) {
		if (result != null && result.size() > 0) {
			String[] str = new String[result.size()];

			for (int i = 0; i < result.size(); i++)
				str[i] = BeanUtil.beanToJson((Object) result.get(i));

			return "json::{\"result\":[" + String.join(",", str) + "]}";
		} else
			return "json::{\"result\": null}";
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public BaseModel getBean() {
		return bean;
	}

	public void setBean(BaseModel bean) {
		this.bean = bean;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getJson() {
		return json;
	}

	public MappingHelper setJson(String json) {
		this.json = json;
		return this;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFromJson(String json, String key, Class<T> clz) {
		Map<String, Object> map = new MappingHelper().setJson(json).toMap();

		if (map != null && map.get(key) != null) {
			return (T) map.get(key);
		}

		return null;
	}
}
